package com.github.lucasmachado.entrepaginas.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.lucasmachado.entrepaginas.ui.cadastro.CadastroScreen
import com.github.lucasmachado.entrepaginas.ui.home.HomeScreen
import com.github.lucasmachado.entrepaginas.ui.intro.IntroScreen
import com.github.lucasmachado.entrepaginas.ui.livros.LivrosScreen
import com.github.lucasmachado.entrepaginas.ui.splash.SplashScreen

@Composable
fun EntrePaginasNavHost() {
    val navController = rememberNavController()

    val voltarParaInicio: () -> Unit = {
        navController.popBackStack(Screen.Home.route, inclusive = false)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onFinish = { introJaVista ->
                    val destino = if (introJaVista) Screen.Home.route else Screen.Intro.route
                    navController.navigate(destino) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Intro.route) {
            IntroScreen(
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onCadastrarClick = {
                    navController.navigate(Screen.Cadastro.route) { launchSingleTop = true }
                },
                onConsultarClick = {
                    navController.navigate(Screen.Livros.route) { launchSingleTop = true }
                },
                onVerIntroducao = {
                    navController.navigate(Screen.Intro.route) { launchSingleTop = true }
                }
            )
        }

        composable(Screen.Cadastro.route) {
            CadastroScreen(
                onVoltar = voltarParaInicio,
                onVerLivros = {
                    navController.navigate(Screen.Livros.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Livros.route) {
            LivrosScreen(
                onVoltar = voltarParaInicio,
                onCadastrarClick = {
                    navController.navigate(Screen.Cadastro.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
