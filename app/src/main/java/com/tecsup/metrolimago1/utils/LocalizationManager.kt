package com.tecsup.metrolimago1.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocalizationManager {
    
    // Idiomas soportados
    const val SPANISH = "es"
    const val ENGLISH = "en"
    
    // Clave para SharedPreferences
    private const val PREF_LANGUAGE = "selected_language"
    
    /**
     * Aplica el idioma seleccionado al contexto
     */
    fun applyLanguage(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }
    
    /**
     * Aplica el idioma al contexto de recursos
     */
    fun applyLanguageToResources(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        
        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
    
    /**
     * Aplica el idioma guardado al contexto de la aplicación
     */
    fun applySavedLanguage(context: Context): Context {
        val savedLanguage = getSavedLanguage(context)
        return applyLanguage(context, savedLanguage)
    }
    
    /**
     * Obtiene el idioma actual guardado en SharedPreferences
     */
    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getString(PREF_LANGUAGE, getSystemLanguage()) ?: getSystemLanguage()
    }
    
    /**
     * Guarda el idioma seleccionado en SharedPreferences
     */
    fun saveLanguage(context: Context, language: String) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_LANGUAGE, language).apply()
    }
    
    /**
     * Obtiene el idioma del sistema
     */
    private fun getSystemLanguage(): String {
        val systemLocale = Locale.getDefault()
        return when (systemLocale.language) {
            "es" -> SPANISH
            "en" -> ENGLISH
            else -> SPANISH // Por defecto español
        }
    }
    
    /**
     * Obtiene el nombre del idioma para mostrar en la UI
     */
    fun getLanguageDisplayName(language: String): String {
        return when (language) {
            SPANISH -> "Español"
            ENGLISH -> "English"
            else -> "Español"
        }
    }
    
    /**
     * Verifica si el idioma está soportado
     */
    fun isLanguageSupported(language: String): Boolean {
        return language in listOf(SPANISH, ENGLISH)
    }
    
    /**
     * Obtiene la lista de idiomas disponibles
     */
    fun getAvailableLanguages(): List<Pair<String, String>> {
        return listOf(
            SPANISH to "Español",
            ENGLISH to "English"
        )
    }
}
