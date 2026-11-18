package com.github.lucasmachado.entrepaginas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaDeCores = lightColorScheme(
    primary = VerdeEstante,
    onPrimary = CremePapel,
    primaryContainer = Salvia,
    onPrimaryContainer = VerdeEstante,
    secondary = Terracota,
    onSecondary = Branco,
    secondaryContainer = Pessego,
    onSecondaryContainer = Color(0xFF5A1F0B),
    tertiary = VerdeFolha,
    onTertiary = Branco,
    tertiaryContainer = Lima,
    onTertiaryContainer = VerdeEstante,
    background = CremePapel,
    onBackground = Tinta,
    surface = CremePapel,
    onSurface = Tinta,
    surfaceVariant = Color(0xFFEFE6D6),
    onSurfaceVariant = VerdeFolha,
    surfaceContainerLowest = Branco,
    surfaceContainerLow = Color(0xFFFFFBF4),
    surfaceContainer = Color(0xFFF8EFE1),
    surfaceContainerHigh = Color(0xFFF3E9D9),
    surfaceContainerHighest = Color(0xFFEDE3D2),
    outline = Contorno,
    error = ErroVermelho,
    onError = Branco
)

@Composable
fun EntrePaginasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaDeCores,
        typography = Typography,
        content = content
    )
}
