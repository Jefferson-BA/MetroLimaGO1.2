package com.tecsup.metrolimago1.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeState = staticCompositionLocalOf { ThemeState() }

class ThemeState {
    var isDarkMode by mutableStateOf(true)
        private set

    var blancoYNegro by mutableStateOf(false)
        private set

    fun toggleTheme() {
        isDarkMode = !isDarkMode
    }

    fun updateDarkMode(isDark: Boolean) {
        isDarkMode = isDark
    }

    fun toggleBlancoYNegro() {
        blancoYNegro = !blancoYNegro
    }

    fun updateBlancoYNegro(isWhiteBlack: Boolean) {
        blancoYNegro = isWhiteBlack
    }
}
