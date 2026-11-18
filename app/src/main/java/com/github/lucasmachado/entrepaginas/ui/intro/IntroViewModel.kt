package com.github.lucasmachado.entrepaginas.ui.intro

import androidx.lifecycle.ViewModel
import com.github.lucasmachado.entrepaginas.data.preferencias.PreferenciasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val preferencias: PreferenciasRepository
) : ViewModel() {

    fun concluirIntro() {
        preferencias.marcarIntroComoVista()
    }
}
