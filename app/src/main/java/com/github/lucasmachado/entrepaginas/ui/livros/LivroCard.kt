package com.github.lucasmachado.entrepaginas.ui.livros

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.lucasmachado.entrepaginas.R
import com.github.lucasmachado.entrepaginas.data.local.LivroEntity
import com.github.lucasmachado.entrepaginas.data.local.StatusLivro
import com.github.lucasmachado.entrepaginas.ui.components.Etiqueta
import com.github.lucasmachado.entrepaginas.ui.theme.CoresCapa
import com.github.lucasmachado.entrepaginas.ui.theme.CremePapel
import com.github.lucasmachado.entrepaginas.ui.theme.Lima
import com.github.lucasmachado.entrepaginas.ui.theme.Pessego
import com.github.lucasmachado.entrepaginas.ui.theme.SalviaClara
import com.github.lucasmachado.entrepaginas.ui.theme.Terracota
import com.github.lucasmachado.entrepaginas.ui.theme.VerdeEstante

@Composable
fun LivroCard(
    livro: LivroEntity,
    onSolicitarTroca: () -> Unit,
    onCancelarSolicitacao: () -> Unit,
    onConcluirTroca: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                CapaLivro(titulo = livro.titulo)
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = livro.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = livro.autor,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = livro.dono,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Etiqueta(
                            texto = livro.estado.rotulo,
                            corFundo = SalviaClara,
                            corTexto = VerdeEstante
                        )
                        EtiquetaStatus(livro.status)
                    }
                }
                IconButton(onClick = onExcluir) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Excluir ${livro.titulo}",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            when (livro.status) {
                StatusLivro.DISPONIVEL -> Button(
                    onClick = onSolicitarTroca,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painterResource(R.drawable.ic_troca),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Solicitar troca")
                }

                StatusLivro.TROCA_SOLICITADA -> Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Aviso(
                        texto = "${livro.solicitante} ofereceu \"${livro.livroOferecido}\". " +
                            "Aguardando ${livro.dono} confirmar.",
                        corFundo = Pessego
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = onCancelarSolicitacao,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                        Button(
                            onClick = onConcluirTroca,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Confirmar troca")
                        }
                    }
                }

                StatusLivro.TROCADO -> Aviso(
                    texto = "Troca concluída: ${livro.dono} trocou com ${livro.solicitante} " +
                        "por \"${livro.livroOferecido}\".",
                    corFundo = Lima.copy(alpha = 0.45f),
                    mostrarIconeConcluido = true
                )
            }
        }
    }
}

@Composable
private fun CapaLivro(titulo: String) {
    val cor = CoresCapa[titulo.hashCode().mod(CoresCapa.size)]
    Box(
        modifier = Modifier
            .width(56.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 10.dp, bottomEnd = 10.dp))
            .background(cor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painterResource(R.drawable.ic_livro),
            contentDescription = null,
            tint = CremePapel,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun EtiquetaStatus(status: StatusLivro) {
    when (status) {
        StatusLivro.DISPONIVEL -> Etiqueta(status.rotulo, corFundo = Lima, corTexto = VerdeEstante)
        StatusLivro.TROCA_SOLICITADA -> Etiqueta(status.rotulo, corFundo = Pessego, corTexto = Terracota)
        StatusLivro.TROCADO -> Etiqueta(
            status.rotulo,
            corFundo = MaterialTheme.colorScheme.surfaceVariant,
            corTexto = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun Aviso(
    texto: String,
    corFundo: Color,
    mostrarIconeConcluido: Boolean = false
) {
    Surface(
        color = corFundo,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mostrarIconeConcluido) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = VerdeEstante,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
