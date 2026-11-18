package com.github.lucasmachado.entrepaginas.data.preferencias

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenciasRepository @Inject constructor(
    private val preferences: SharedPreferences
) {
    companion object {
        private const val KEY_INTRO_VISTA = "intro_vista"
    }

    fun introJaVista(): Boolean = preferences.getBoolean(KEY_INTRO_VISTA, false)

    fun marcarIntroComoVista() {
        preferences.edit().putBoolean(KEY_INTRO_VISTA, true).apply()
    }
}
