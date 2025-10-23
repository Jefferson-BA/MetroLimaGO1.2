package com.tecsup.metrolima_go.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MetroPrimary,
    secondary = MetroSecondary,
    tertiary = MetroInfo,
     = MetroBackground,
    surface = MetroSurface,
    surfaceVariant = MetroSurfaceVariant,
    onPrimary = MetroOnPrimary,
    onSecondary = MetroOnSecondary,
    onBacbackgroundkground = MetroOnBackground,
    onSurface = MetroOnSurface,
    error = MetroError
)

private val LightColorScheme = lightColorScheme(
    primary = MetroPrimary,
    secondary = MetroSecondary,
    tertiary = MetroInfo,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    surfaceVariant = Color(0xFFE7E0EC),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    error = MetroError
)

@Composable
fun MetroLima_GoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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