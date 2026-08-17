package com.example.luminalearn.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary            = Primary,
    onPrimary          = White,
    primaryContainer   = PrimaryLight,
    onPrimaryContainer = Primary,

    secondary            = Accent,
    onSecondary          = White,
    secondaryContainer   = AccentLight,
    onSecondaryContainer = Accent,

    tertiary            = Success,
    onTertiary          = TextDark,
    tertiaryContainer   = SuccessLight,
    onTertiaryContainer = TextDark,

    error            = ErrorRed,
    onError          = White,
    errorContainer   = ErrorLight,

    background       = Background,
    onBackground     = TextDark,

    surface          = Surface,
    onSurface        = TextDark,
    onSurfaceVariant = TextMuted,
)

private val DarkColorScheme = darkColorScheme(
    primary            = PrimaryLight,
    onPrimary          = Primary,
    primaryContainer   = Primary,
    onPrimaryContainer = PrimaryLight,

    secondary            = Accent,
    onSecondary          = TextDark,
    secondaryContainer   = AccentLight,
    onSecondaryContainer = TextDark,

    error            = ErrorRed,
    onError          = White,

    background       = Color(0xFF0D1226),
    onBackground     = White,

    surface          = Color(0xFF151A2F),
    onSurface        = White,
    onSurfaceVariant = TextMuted,
)


@Composable
fun LuminaLearnTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

