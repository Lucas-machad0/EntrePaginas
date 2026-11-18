package com.github.lucasmachado.entrepaginas.ui.intro

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.lucasmachado.entrepaginas.R
import com.github.lucasmachado.entrepaginas.ui.theme.Lima
import kotlinx.coroutines.launch

private data class PaginaIntro(
    val icone: @Composable () -> Painter,
    val titulo: String,
    val descricao: String,
    val destaque: String? = null
)

private val paginas = listOf(
    PaginaIntro(
        icone = { painterResource(R.drawable.ic_livro) },
        titulo = "Livros parados, leituras possíveis",
        descricao = "Muitos livros ficam esquecidos na estante enquanto alguém " +
            "por perto procura uma nova leitura. O EntrePáginas aproxima essas pessoas.",
        destaque = "53% da população com 5 anos ou mais não leu nem parte de um livro " +
            "nos três meses anteriores à pesquisa (Retratos da Leitura no Brasil, 2024)."
    ),
    PaginaIntro(
        icone = { rememberVectorPainter(Icons.Filled.Edit) },
        titulo = "Cadastre seus livros",
        descricao = "Informe seu nome, o título, o autor e o estado de conservação " +
            "do exemplar que você quer colocar para troca."
    ),
    PaginaIntro(
        icone = { painterResource(R.drawable.ic_troca) },
        titulo = "Solicite uma troca",
        descricao = "Consulte os livros da comunidade, escolha o que você quer ler " +
            "e ofereça um dos seus em troca."
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroScreen(
    onFinish: () -> Unit,
    viewModel: IntroViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { paginas.size })
    val scope = rememberCoroutineScope()
    val ultimaPagina = pagerState.currentPage == paginas.lastIndex

    val concluir: () -> Unit = {
        viewModel.concluirIntro()
        onFinish()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { indice ->
            ConteudoPagina(paginas[indice])
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            paginas.indices.forEach { indice ->
                Indicador(selecionado = pagerState.currentPage == indice)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!ultimaPagina) {
                TextButton(onClick = concluir) {
                    Text("Pular")
                }
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    if (ultimaPagina) {
                        concluir()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.height(52.dp)
            ) {
                Text(if (ultimaPagina) "Começar" else "Próximo")
            }
        }
    }
}

@Composable
private fun ConteudoPagina(pagina: PaginaIntro) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(MaterialTheme.colorScheme.primary)
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(176.dp)
                    .clip(CircleShape)
                    .background(Lima.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = pagina.icone(),
                    contentDescription = null,
                    tint = Lima,
                    modifier = Modifier.size(88.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 28.dp, top = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = pagina.titulo,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = pagina.descricao,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (pagina.destaque != null) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = pagina.destaque,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Indicador(selecionado: Boolean) {
    val largura by animateDpAsState(
        targetValue = if (selecionado) 28.dp else 8.dp,
        label = "larguraIndicador"
    )
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .height(8.dp)
            .width(largura)
            .clip(CircleShape)
            .background(
                if (selecionado) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.primaryContainer
            )
    )
}
