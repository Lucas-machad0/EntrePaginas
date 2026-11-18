package com.github.lucasmachado.entrepaginas.ui.splash

import androidx.lifecycle.ViewModel
import com.github.lucasmachado.entrepaginas.data.preferencias.PreferenciasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencias: PreferenciasRepository
) : ViewModel() {

    fun introJaVista(): Boolean = preferencias.introJaVista()
}
