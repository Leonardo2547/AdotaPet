package com.example.adotapet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaClaro = lightColorScheme(
    primary = VerdeClaro,
    secondary = VerdeClaro,
    primaryContainer = VerdeContainerClaro,
    background = FundoClaro
)

private val EsquemaEscuro = darkColorScheme(
    primary = VerdeEscuro,
    secondary = VerdeEscuro,
    primaryContainer = VerdeContainerEscuro,
    background = FundoEscuro
)

@Composable
fun AdotaPetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaEscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}
