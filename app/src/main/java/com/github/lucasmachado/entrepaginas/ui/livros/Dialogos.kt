package com.github.lucasmachado.entrepaginas.ui.livros

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.github.lucasmachado.entrepaginas.R
import com.github.lucasmachado.entrepaginas.data.local.LivroEntity
import com.github.lucasmachado.entrepaginas.ui.components.CampoTexto

@Composable
fun SolicitarTrocaDialog(
    livro: LivroEntity,
    onConfirmar: (solicitante: String, livroOferecido: String) -> Unit,
    onDismiss: () -> Unit
) {
    var solicitante by rememberSaveable { mutableStateOf("") }
    var oferecido by rememberSaveable { mutableStateOf("") }
    var erroSolicitante by remember { mutableStateOf<String?>(null) }
    var erroOferecido by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(painterResource(R.drawable.ic_troca), contentDescription = null) },
        title = { Text("Solicitar troca") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Você quer \"${livro.titulo}\", de ${livro.dono}. " +
                        "Qual livro você oferece em troca?",
                    style = MaterialTheme.typography.bodyMedium
                )
                CampoTexto(
                    valor = solicitante,
                    onValorChange = {
                        solicitante = it
                        erroSolicitante = null
                    },
                    rotulo = "Seu nome *",
                    erro = erroSolicitante,
                    capitalizacao = KeyboardCapitalization.Words,
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }
                )
                CampoTexto(
                    valor = oferecido,
                    onValorChange = {
                        oferecido = it
                        erroOferecido = null
                    },
                    rotulo = "Livro que você oferece *",
                    erro = erroOferecido,
                    imeAction = ImeAction.Done,
                    leadingIcon = {
                        Icon(painterResource(R.drawable.ic_livro), contentDescription = null)
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val nome = solicitante.trim()
                    val livroOferecido = oferecido.trim()
                    erroSolicitante = when {
                        nome.isEmpty() -> "Informe seu nome"
                        nome.equals(livro.dono.trim(), ignoreCase = true) ->
                            "Este livro já é seu"
                        else -> null
                    }
                    erroOferecido = if (livroOferecido.isEmpty()) "Informe o livro oferecido" else null

                    if (erroSolicitante == null && erroOferecido == null) {
                        onConfirmar(nome, livroOferecido)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Enviar proposta")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun ConfirmarExclusaoDialog(
    livro: LivroEntity,
    onConfirmar: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
        title = { Text("Excluir livro?") },
        text = {
            Text("\"${livro.titulo}\", de ${livro.dono}, será removido da lista. Essa ação não pode ser desfeita.")
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Excluir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
