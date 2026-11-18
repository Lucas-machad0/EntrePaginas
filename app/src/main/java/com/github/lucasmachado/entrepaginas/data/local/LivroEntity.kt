package com.github.lucasmachado.entrepaginas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EstadoConservacao(val rotulo: String, val descricao: String) {
    NOVO("Novo", "Sem sinais de uso."),
    SEMINOVO("Seminovo", "Poucos sinais de uso, tudo inteiro."),
    USADO("Usado", "Marcas de uso, mas páginas completas."),
    DESGASTADO("Desgastado", "Capa ou páginas gastas, ainda legível.")
}

enum class StatusLivro(val rotulo: String) {
    DISPONIVEL("Disponível"),
    TROCA_SOLICITADA("Troca solicitada"),
    TROCADO("Trocado")
}

@Entity(tableName = "livros")
data class LivroEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val autor: String,
    val dono: String,
    val estado: EstadoConservacao,
    val status: StatusLivro = StatusLivro.DISPONIVEL,
    val solicitante: String? = null,
    val livroOferecido: String? = null,
    val cadastradoEm: Long = System.currentTimeMillis()
)
