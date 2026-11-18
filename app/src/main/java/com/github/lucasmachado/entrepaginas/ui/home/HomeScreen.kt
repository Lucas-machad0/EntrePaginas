package com.github.lucasmachado.entrepaginas.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.lucasmachado.entrepaginas.R
import com.github.lucasmachado.entrepaginas.ui.theme.Branco
import com.github.lucasmachado.entrepaginas.ui.theme.CremePapel
import com.github.lucasmachado.entrepaginas.ui.theme.Lima

@Composable
fun HomeScreen(
    onCadastrarClick: () -> Unit,
    onConsultarClick: () -> Unit,
    onVerIntroducao: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val resumo by viewModel.resumo.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Cabecalho(resumo)
            ConteudoInicio(
                onCadastrarClick = onCadastrarClick,
                onConsultarClick = onConsultarClick,
                onVerIntroducao = onVerIntroducao
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
private fun ConteudoInicio(
    onCadastrarClick: () -> Unit,
    onConsultarClick: () -> Unit,
    onVerIntroducao: () -> Unit
) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "O que você quer fazer?",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        CardAcao(
            icone = Icons.Filled.Add,
            corIcone = MaterialTheme.colorScheme.secondary,
            titulo = "Cadastrar livro",
            descricao = "Informe seu nome, o livro e o estado de conservação.",
            onClick = onCadastrarClick
        )

        CardAcao(
            icone = Icons.Filled.Search,
            corIcone = MaterialTheme.colorScheme.primary,
            titulo = "Consultar livros",
            descricao = "Veja os livros da comunidade e solicite uma troca.",
            onClick = onConsultarClick
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Por que trocar livros?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "O EntrePáginas aproxima quem tem um livro parado na estante de " +
                "quem procura uma nova leitura, na mesma comunidade.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            CardOds(
                numero = "4",
                descricao = "Educação de qualidade",
                modifier = Modifier.weight(1f)
            )
            CardOds(
                numero = "11",
                descricao = "Cidades e comunidades sustentáveis",
                modifier = Modifier.weight(1f)
            )
        }

        TextButton(
            onClick = onVerIntroducao,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Rever a introdução")
        }

        Spacer(Modifier.navigationBarsPadding())
    }
}

@Composable
private fun Cabecalho(resumo: ResumoHome) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(MaterialTheme.colorScheme.primary)
            .statusBarsPadding()
            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 28.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_livro),
                contentDescription = null,
                tint = Lima,
                modifier = Modifier.size(34.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = buildAnnotatedString {
                    append("Entre")
                    withStyle(SpanStyle(color = Lima)) { append("Páginas") }
                },
                style = MaterialTheme.typography.headlineSmall,
                color = CremePapel
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Seu livro pode ser a próxima leitura de alguém.",
            style = MaterialTheme.typography.bodyLarge,
            color = CremePapel
        )
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IndicadorResumo(resumo.disponiveis, "disponíveis", Modifier.weight(1f))
            IndicadorResumo(resumo.emTroca, "em troca", Modifier.weight(1f))
            IndicadorResumo(resumo.trocados, "trocados", Modifier.weight(1f))
        }
    }
}

@Composable
private fun IndicadorResumo(valor: Int, rotulo: String, modifier: Modifier = Modifier) {
    Surface(
        color = Branco.copy(alpha = 0.08f),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(
                text = valor.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = Lima
            )
            Text(
                text = rotulo,
                style = MaterialTheme.typography.bodyMedium,
                color = CremePapel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardAcao(
    icone: ImageVector,
    corIcone: Color,
    titulo: String,
    descricao: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(corIcone),
                contentAlignment = Alignment.Center
            ) {
                Icon(icone, contentDescription = null, tint = Branco)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CardOds(numero: String, descricao: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxHeight()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "ODS $numero",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = descricao,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
