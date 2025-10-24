package com.tecsup.metrolimago1.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun GradientBackground(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    // Usar colores sólidos como en el planificador de rutas
    val backgroundColor = if (isDarkMode) DarkGray else Color(0xFFF5F5F5)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        content()
    }
}
