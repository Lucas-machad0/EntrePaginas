package com.github.lucasmachado.entrepaginas.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object LivrosMock {
    val livros = listOf(
        LivroEntity(titulo = "Vidas Secas", autor = "Graciliano Ramos", dono = "Ana Souza", estado = EstadoConservacao.SEMINOVO),
        LivroEntity(titulo = "O Cortiço", autor = "Aluísio Azevedo", dono = "Carlos Lima", estado = EstadoConservacao.USADO),
        LivroEntity(titulo = "A Hora da Estrela", autor = "Clarice Lispector", dono = "Beatriz Rocha", estado = EstadoConservacao.NOVO),
        LivroEntity(titulo = "Capitães da Areia", autor = "Jorge Amado", dono = "Diego Martins", estado = EstadoConservacao.DESGASTADO),
        LivroEntity(titulo = "Quarto de Despejo", autor = "Carolina Maria de Jesus", dono = "Mariana Costa", estado = EstadoConservacao.SEMINOVO),
        LivroEntity(titulo = "Memórias Póstumas de Brás Cubas", autor = "Machado de Assis", dono = "João Pereira", estado = EstadoConservacao.USADO),
        LivroEntity(titulo = "O Pequeno Príncipe", autor = "Antoine de Saint-Exupéry", dono = "Fernanda Alves", estado = EstadoConservacao.NOVO),
        LivroEntity(titulo = "Torto Arado", autor = "Itamar Vieira Junior", dono = "Rafael Nunes", estado = EstadoConservacao.SEMINOVO)
    )
}

class PopularLivrosMockCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val agora = System.currentTimeMillis()
        LivrosMock.livros.forEachIndexed { indice, livro ->
            db.execSQL(
                "INSERT INTO livros (titulo, autor, dono, estado, status, cadastradoEm) VALUES (?, ?, ?, ?, ?, ?)",
                arrayOf<Any>(
                    livro.titulo,
                    livro.autor,
                    livro.dono,
                    livro.estado.name,
                    livro.status.name,
                    agora - indice * 60_000L
                )
            )
        }
    }
}
