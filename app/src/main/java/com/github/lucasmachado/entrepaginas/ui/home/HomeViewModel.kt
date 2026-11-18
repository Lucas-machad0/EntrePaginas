package com.github.lucasmachado.entrepaginas.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucasmachado.entrepaginas.data.local.StatusLivro
import com.github.lucasmachado.entrepaginas.data.repository.LivroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ResumoHome(
    val disponiveis: Int = 0,
    val emTroca: Int = 0,
    val trocados: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: LivroRepository
) : ViewModel() {

    val resumo: StateFlow<ResumoHome> = repository.listarLivros()
        .map { livros ->
            ResumoHome(
                disponiveis = livros.count { it.status == StatusLivro.DISPONIVEL },
                emTroca = livros.count { it.status == StatusLivro.TROCA_SOLICITADA },
                trocados = livros.count { it.status == StatusLivro.TROCADO }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ResumoHome())
}
