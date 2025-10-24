package com.tecsup.metrolimago1.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MetroOrange,
    secondary = LightGray,
    tertiary = MetroGreen,
    background = DarkGray,
    surface = CardGray,
    onBackground = White,
    onSurface = White,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White
)

private val LightColorScheme = lightColorScheme(
    primary = MetroOrange,
    secondary = LightTextSecondary,
    tertiary = MetroGreen,
    background = LightBackground,
    surface = LightCard,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary,
    onPrimary = White,
    onSecondary = LightTextPrimary,
    onTertiary = White
)

@Composable
fun MetroLimaGO1Theme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    blancoYNegro: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val adjustedScheme = if (darkTheme) {
        colorScheme.copy(
            surface = Color.White.copy(alpha = 0.08f),
            background = Color(0xFF0E0E0E)
        )
    } else {
        colorScheme
    }

    MaterialTheme(
        colorScheme = adjustedScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}