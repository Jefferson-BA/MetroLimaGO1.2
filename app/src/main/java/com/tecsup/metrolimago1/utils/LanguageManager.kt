package com.tecsup.metrolimago1.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

class LanguageManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)

    var currentLanguage by mutableStateOf(getStoredLanguage())

    fun getStoredLanguage(): String {
        return prefs.getString("selected_language", "es") ?: "es"
    }

    fun setLanguage(language: String) {
        currentLanguage = language
        prefs.edit().putString("selected_language", language).apply()
    }

    fun restartActivity(activity: Activity) {
        val intent = Intent(activity, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
        activity.finish()
    }

    fun getLocale(): Locale {
        return when (currentLanguage) {
            "en" -> Locale.ENGLISH
            "es" -> Locale("es", "PE") // Español de Perú
            else -> Locale("es", "PE")
        }
    }

    fun updateConfiguration(): Configuration {
        val locale = getLocale()
        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        return config
    }
    
    fun applyLanguageToContext(): Context {
        val locale = getLocale()
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}

val LocalLanguageManager = staticCompositionLocalOf<LanguageManager> {
    error("No LanguageManager provided")
}

@Composable
fun rememberLanguageManager(): LanguageManager {
    val context = LocalContext.current
    return remember { LanguageManager(context) }
}
