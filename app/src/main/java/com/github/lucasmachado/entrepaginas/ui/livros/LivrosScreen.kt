package com.github.lucasmachado.entrepaginas.ui.livros

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.lucasmachado.entrepaginas.R
import com.github.lucasmachado.entrepaginas.data.local.LivroEntity
import com.github.lucasmachado.entrepaginas.ui.components.EntrePaginasTopBar
import com.github.lucasmachado.entrepaginas.ui.components.EstadoVazio

@Composable
fun LivrosScreen(
    onVoltar: () -> Unit,
    onCadastrarClick: () -> Unit,
    viewModel: LivrosViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val busca by viewModel.busca.collectAsState()
    val mensagem by viewModel.mensagem.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var livroParaTroca by remember { mutableStateOf<LivroEntity?>(null) }
    var livroParaExcluir by remember { mutableStateOf<LivroEntity?>(null) }

    LaunchedEffect(mensagem) {
        val texto = mensagem ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(texto)
        viewModel.mensagemExibida()
    }

    Scaffold(
        topBar = { EntrePaginasTopBar(titulo = "Livros da comunidade", onVoltar = onVoltar) },
        snackbarHost = { SnackbarHost(snackbarHostState, modifier = Modifier.imePadding()) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCadastrarClick,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Cadastrar") },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = busca,
                onValueChange = viewModel::onBuscaChange,
                placeholder = { Text("Buscar por título, autor ou dono") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = if (busca.isNotEmpty()) {
                    {
                        IconButton(onClick = { viewModel.onBuscaChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Limpar busca")
                        }
                    }
                } else {
                    null
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            when (val estado = uiState) {
                is LivrosUiState.Carregando -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is LivrosUiState.Sucesso -> when {
                    estado.totalCadastrados == 0 -> EstadoVazio(
                        icone = painterResource(R.drawable.ic_livro),
                        titulo = "Nenhum livro cadastrado",
                        descricao = "Cadastre o primeiro livro para começar as trocas na comunidade.",
                        acao = {
                            Button(onClick = onCadastrarClick) { Text("Cadastrar livro") }
                        }
                    )

                    estado.livros.isEmpty() -> EstadoVazio(
                        icone = rememberVectorPainter(Icons.Filled.Search),
                        titulo = "Nenhum livro encontrado",
                        descricao = "Não há resultados para \"${busca.trim()}\". Tente outro título, autor ou nome."
                    )

                    else -> LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = textoQuantidade(estado.livros.size),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        items(estado.livros, key = { it.id }) { livro ->
                            LivroCard(
                                livro = livro,
                                onSolicitarTroca = { livroParaTroca = livro },
                                onCancelarSolicitacao = { viewModel.cancelarSolicitacao(livro) },
                                onConcluirTroca = { viewModel.concluirTroca(livro) },
                                onExcluir = { livroParaExcluir = livro }
                            )
                        }
                    }
                }
            }
        }
    }

    livroParaTroca?.let { livro ->
        SolicitarTrocaDialog(
            livro = livro,
            onConfirmar = { solicitante, oferecido ->
                viewModel.solicitarTroca(livro, solicitante, oferecido)
                livroParaTroca = null
            },
            onDismiss = { livroParaTroca = null }
        )
    }

    livroParaExcluir?.let { livro ->
        ConfirmarExclusaoDialog(
            livro = livro,
            onConfirmar = {
                viewModel.excluir(livro)
                livroParaExcluir = null
            },
            onDismiss = { livroParaExcluir = null }
        )
    }
}

private fun textoQuantidade(quantidade: Int): String =
    if (quantidade == 1) "1 livro" else "$quantidade livros"
