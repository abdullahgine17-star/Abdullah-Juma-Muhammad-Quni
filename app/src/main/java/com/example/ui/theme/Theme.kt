package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = IdeBluePrimary,
    onPrimary = Color.White,
    secondary = IdePurpleKeyword,
    tertiary = IdeCyanType,
    background = IdeDarkBg,
    surface = IdeDarkSurface,
    surfaceVariant = IdeDarkHeader,
    onBackground = Color(0xFFABB2BF),
    onSurface = Color(0xFFABB2BF),
    outline = IdeDarkBorder,
    error = IdeRedError
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0066CC),
    onPrimary = Color.White,
    secondary = Color(0xFF6B21A8),
    tertiary = Color(0xFF0284C7),
    background = IdeLightBg,
    surface = IdeLightSurface,
    surfaceVariant = IdeLightHeader,
    onBackground = IdeLightText,
    onSurface = IdeLightText,
    outline = IdeLightBorder,
    error = Color(0xFFDC2626)
)

@Composable
fun CodeCraftStudioTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
