package com.github.lucasmachado.entrepaginas.ui.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucasmachado.entrepaginas.data.local.EstadoConservacao
import com.github.lucasmachado.entrepaginas.data.local.LivroEntity
import com.github.lucasmachado.entrepaginas.data.repository.LivroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CadastroUiState(
    val nome: String = "",
    val titulo: String = "",
    val autor: String = "",
    val estado: EstadoConservacao? = null,
    val erroNome: String? = null,
    val erroTitulo: String? = null,
    val erroAutor: String? = null,
    val erroEstado: String? = null,
    val salvando: Boolean = false,
    val mensagem: String? = null
)

@HiltViewModel
class CadastroViewModel @Inject constructor(
    private val repository: LivroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroUiState())
    val uiState: StateFlow<CadastroUiState> = _uiState.asStateFlow()

    fun onNomeChange(valor: String) = _uiState.update { it.copy(nome = valor, erroNome = null) }
    fun onTituloChange(valor: String) = _uiState.update { it.copy(titulo = valor, erroTitulo = null) }
    fun onAutorChange(valor: String) = _uiState.update { it.copy(autor = valor, erroAutor = null) }
    fun onEstadoChange(valor: EstadoConservacao) = _uiState.update { it.copy(estado = valor, erroEstado = null) }

    fun salvar() {
        val atual = _uiState.value
        if (atual.salvando) return

        val nome = atual.nome.trim()
        val titulo = atual.titulo.trim()
        val autor = atual.autor.trim()
        val estado = atual.estado

        val erroNome = when {
            nome.isEmpty() -> "Informe seu nome"
            nome.length < 2 -> "O nome precisa ter pelo menos 2 letras"
            else -> null
        }
        val erroTitulo = when {
            titulo.isEmpty() -> "Informe o título do livro"
            titulo.length < 2 -> "O título precisa ter pelo menos 2 caracteres"
            else -> null
        }
        val erroAutor = if (autor.isEmpty()) "Informe o autor do livro" else null
        val erroEstado = if (estado == null) "Escolha o estado de conservação" else null

        if (erroNome != null || erroTitulo != null || erroAutor != null || estado == null) {
            _uiState.update {
                it.copy(
                    erroNome = erroNome,
                    erroTitulo = erroTitulo,
                    erroAutor = erroAutor,
                    erroEstado = erroEstado
                )
            }
            return
        }

        _uiState.update { it.copy(salvando = true) }
        viewModelScope.launch {
            repository.cadastrar(
                LivroEntity(titulo = titulo, autor = autor, dono = nome, estado = estado)
            )
            _uiState.update {
                CadastroUiState(
                    nome = nome,
                    mensagem = "\"$titulo\" cadastrado com sucesso!"
                )
            }
        }
    }

    fun mensagemExibida() = _uiState.update { it.copy(mensagem = null) }
}
