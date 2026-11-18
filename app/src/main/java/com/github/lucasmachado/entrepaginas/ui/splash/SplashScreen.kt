package com.github.lucasmachado.entrepaginas.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.lucasmachado.entrepaginas.R
import com.github.lucasmachado.entrepaginas.ui.theme.CremePapel
import com.github.lucasmachado.entrepaginas.ui.theme.Lima
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinish: (introJaVista: Boolean) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val opacidade = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        opacidade.animateTo(1f, animationSpec = tween(durationMillis = 700))
        delay(1_300)
        onFinish(viewModel.introJaVista())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(opacidade.value)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_livro),
                contentDescription = null,
                tint = Lima,
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Entre",
                style = MaterialTheme.typography.displaySmall,
                color = CremePapel
            )
            Text(
                text = "Páginas",
                style = MaterialTheme.typography.displaySmall,
                color = Lima
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Troca de livros na sua comunidade",
                style = MaterialTheme.typography.bodyLarge,
                color = CremePapel
            )
        }

        CircularProgressIndicator(
            color = Lima,
            strokeWidth = 3.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .size(28.dp)
        )
    }
}
