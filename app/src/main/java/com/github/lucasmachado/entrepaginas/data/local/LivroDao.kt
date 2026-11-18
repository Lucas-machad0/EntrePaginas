package com.github.lucasmachado.entrepaginas.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LivroDao {

    @Query("SELECT * FROM livros ORDER BY cadastradoEm DESC")
    fun listarTodos(): Flow<List<LivroEntity>>

    @Insert
    suspend fun inserir(livro: LivroEntity): Long

    @Update
    suspend fun atualizar(livro: LivroEntity)

    @Delete
    suspend fun excluir(livro: LivroEntity)
}
