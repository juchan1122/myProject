package com.juchan.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    primaryContainer = IndigoContainer,
    onPrimaryContainer = IndigoDark,
    secondary = Mint,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextMuted,
    error = Coral
)

private val DarkColors = darkColorScheme(
    primary = IndigoLight,
    onPrimary = Color(0xFF14141F),
    primaryContainer = IndigoDark,
    onPrimaryContainer = IndigoContainer,
    secondary = Mint,
    background = BackgroundDark,
    onBackground = Color(0xFFE8E8F0),
    surface = SurfaceDark,
    onSurface = Color(0xFFE8E8F0),
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = Color(0xFFA0A0B8),
    error = Coral
)

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // 폰 설정에 따라 자동으로 다크모드가 적용
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}