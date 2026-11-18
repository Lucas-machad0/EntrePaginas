package com.github.lucasmachado.entrepaginas.ui.cadastro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.lucasmachado.entrepaginas.R
import com.github.lucasmachado.entrepaginas.data.local.EstadoConservacao
import com.github.lucasmachado.entrepaginas.ui.components.CampoTexto
import com.github.lucasmachado.entrepaginas.ui.components.EntrePaginasTopBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CadastroScreen(
    onVoltar: () -> Unit,
    onVerLivros: () -> Unit,
    viewModel: CadastroViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.mensagem) {
        val mensagem = uiState.mensagem ?: return@LaunchedEffect
        val resultado = snackbarHostState.showSnackbar(
            message = mensagem,
            actionLabel = "Ver livros",
            duration = SnackbarDuration.Short
        )
        viewModel.mensagemExibida()
        if (resultado == SnackbarResult.ActionPerformed) onVerLivros()
    }

    Scaffold(
        topBar = { EntrePaginasTopBar(titulo = "Cadastrar livro", onVoltar = onVoltar) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Conte qual livro você quer colocar para troca. " +
                    "Os campos com * são obrigatórios.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            CampoTexto(
                valor = uiState.nome,
                onValorChange = viewModel::onNomeChange,
                rotulo = "Seu nome *",
                erro = uiState.erroNome,
                capitalizacao = KeyboardCapitalization.Words,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }
            )

            CampoTexto(
                valor = uiState.titulo,
                onValorChange = viewModel::onTituloChange,
                rotulo = "Título do livro *",
                erro = uiState.erroTitulo,
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_livro), contentDescription = null)
                }
            )

            CampoTexto(
                valor = uiState.autor,
                onValorChange = viewModel::onAutorChange,
                rotulo = "Autor *",
                erro = uiState.erroAutor,
                capitalizacao = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done,
                leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) }
            )

            Text(
                text = "Estado de conservação *",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EstadoConservacao.entries.forEach { opcao ->
                    val selecionado = uiState.estado == opcao
                    FilterChip(
                        selected = selecionado,
                        onClick = { viewModel.onEstadoChange(opcao) },
                        label = { Text(opcao.rotulo) },
                        leadingIcon = if (selecionado) {
                            {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }

            val estadoSelecionado = uiState.estado
            val erroEstado = uiState.erroEstado
            when {
                erroEstado != null -> Text(
                    text = erroEstado,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )

                estadoSelecionado != null -> Text(
                    text = estadoSelecionado.descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.salvar()
                },
                enabled = !uiState.salvando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cadastrar livro")
            }

            OutlinedButton(
                onClick = onVerLivros,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Ver livros cadastrados")
            }
        }
    }
}
