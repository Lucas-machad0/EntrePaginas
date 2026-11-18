package com.github.lucasmachado.entrepaginas.di

import android.content.Context
import androidx.room.Room
import com.github.lucasmachado.entrepaginas.data.local.AppDatabase
import com.github.lucasmachado.entrepaginas.data.local.LivroDao
import com.github.lucasmachado.entrepaginas.data.local.PopularLivrosMockCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "entre-paginas.db")
            .addCallback(PopularLivrosMockCallback())
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    @Singleton
    fun provideLivroDao(database: AppDatabase): LivroDao = database.livroDao()
}
