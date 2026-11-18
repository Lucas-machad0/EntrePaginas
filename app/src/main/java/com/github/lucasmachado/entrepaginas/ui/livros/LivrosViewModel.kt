package com.github.lucasmachado.entrepaginas.ui.livros

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucasmachado.entrepaginas.data.local.LivroEntity
import com.github.lucasmachado.entrepaginas.data.repository.LivroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LivrosUiState {
    data object Carregando : LivrosUiState()

    data class Sucesso(
        val livros: List<LivroEntity>,
        val totalCadastrados: Int
    ) : LivrosUiState()
}

@HiltViewModel
class LivrosViewModel @Inject constructor(
    private val repository: LivroRepository
) : ViewModel() {

    private val _busca = MutableStateFlow("")
    val busca: StateFlow<String> = _busca.asStateFlow()

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem.asStateFlow()

    val uiState: StateFlow<LivrosUiState> =
        combine(repository.listarLivros(), _busca) { livros, termo ->
            val filtro = termo.trim()
            val filtrados = if (filtro.isEmpty()) {
                livros
            } else {
                livros.filter { livro ->
                    livro.titulo.contains(filtro, ignoreCase = true) ||
                        livro.autor.contains(filtro, ignoreCase = true) ||
                        livro.dono.contains(filtro, ignoreCase = true)
                }
            }
            LivrosUiState.Sucesso(livros = filtrados, totalCadastrados = livros.size)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LivrosUiState.Carregando)

    fun onBuscaChange(valor: String) {
        _busca.value = valor
    }

    fun solicitarTroca(livro: LivroEntity, solicitante: String, livroOferecido: String) {
        viewModelScope.launch {
            repository.solicitarTroca(livro, solicitante, livroOferecido)
            _mensagem.value = "Troca solicitada! Aguardando ${livro.dono} confirmar."
        }
    }

    fun cancelarSolicitacao(livro: LivroEntity) {
        viewModelScope.launch {
            repository.cancelarSolicitacao(livro)
            _mensagem.value = "Solicitação cancelada. \"${livro.titulo}\" voltou a ficar disponível."
        }
    }

    fun concluirTroca(livro: LivroEntity) {
        viewModelScope.launch {
            repository.concluirTroca(livro)
            _mensagem.value = "Troca concluída! Boa leitura."
        }
    }

    fun excluir(livro: LivroEntity) {
        viewModelScope.launch {
            repository.excluir(livro)
            _mensagem.value = "\"${livro.titulo}\" foi excluído."
        }
    }

    fun mensagemExibida() {
        _mensagem.value = null
    }
}
