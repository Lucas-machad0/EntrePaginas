package com.github.lucasmachado.entrepaginas.data.repository

import com.github.lucasmachado.entrepaginas.data.local.LivroDao
import com.github.lucasmachado.entrepaginas.data.local.LivroEntity
import com.github.lucasmachado.entrepaginas.data.local.StatusLivro
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LivroRepository @Inject constructor(
    private val dao: LivroDao
) {

    fun listarLivros(): Flow<List<LivroEntity>> = dao.listarTodos()

    suspend fun cadastrar(livro: LivroEntity) {
        dao.inserir(livro)
    }

    suspend fun solicitarTroca(livro: LivroEntity, solicitante: String, livroOferecido: String) {
        dao.atualizar(
            livro.copy(
                status = StatusLivro.TROCA_SOLICITADA,
                solicitante = solicitante,
                livroOferecido = livroOferecido
            )
        )
    }

    suspend fun cancelarSolicitacao(livro: LivroEntity) {
        dao.atualizar(
            livro.copy(status = StatusLivro.DISPONIVEL, solicitante = null, livroOferecido = null)
        )
    }

    suspend fun concluirTroca(livro: LivroEntity) {
        dao.atualizar(livro.copy(status = StatusLivro.TROCADO))
    }

    suspend fun excluir(livro: LivroEntity) {
        dao.excluir(livro)
    }
}
