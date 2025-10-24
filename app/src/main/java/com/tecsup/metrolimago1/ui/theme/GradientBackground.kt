package com.tecsup.metrolimago1.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GradientBackground(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    // Definir los gradientes para modo claro y oscuro
    val gradientColors = if (isDarkMode) {
        listOf(
            Color(0xFF000000),
            Color(0xFF1C1C1C),
            Color(0xFFF8730C)
        )
    } else {
        listOf(
            Color(0xFFFFF8E1),
            Color(0xFFFFF9E9),
            Color(0xFFF8730C)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(gradientColors)
            )
    ) {
        content()
    }
}
