package com.tecsup.metrolimago1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
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
    secondary = LightGray,
    tertiary = MetroGreen,
    background = Color(0xFFF5F5F5), // Fondo claro
    surface = Color(0xFFFFFFFF), // Superficie blanca
    onBackground = Color(0xFF1C1C1C), // Texto oscuro
    onSurface = Color(0xFF1C1C1C), // Texto oscuro
    onPrimary = White,
    onSecondary = Color(0xFF1C1C1C),
    onTertiary = White
)

@Composable
fun MetroLimaGO1Theme(
    darkTheme: Boolean = true, // Modo oscuro por defecto
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Desactivar colores dinámicos para mantener diseño consistente
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}