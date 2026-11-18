package com.github.lucasmachado.entrepaginas.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Intro : Screen("intro")
    object Home : Screen("home")
    object Cadastro : Screen("cadastro")
    object Livros : Screen("livros")
}
