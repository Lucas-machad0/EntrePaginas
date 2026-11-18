package com.github.lucasmachado.entrepaginas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LivroEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun livroDao(): LivroDao
}
