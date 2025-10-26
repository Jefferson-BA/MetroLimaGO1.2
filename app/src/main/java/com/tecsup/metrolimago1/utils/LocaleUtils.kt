package com.tecsup.metrolimago1.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleUtils {
    
    /**
     * Actualiza el locale del contexto
     * @param context Contexto actual
     * @param locale Nuevo locale
     * @return Contexto actualizado
     */
    fun updateLocale(context: Context, locale: Locale): Context {
        val config = Configuration(context.resources.configuration)
        Locale.setDefault(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        } else {
            config.locale = locale
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            return context
        }
    }
    
    /**
     * Obtiene el locale actual del contexto
     * @param context Contexto de la aplicación
     * @return Locale actual
     */
    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }
    
    /**
     * Verifica si el idioma actual es español
     * @param context Contexto de la aplicación
     * @return true si es español, false si es inglés
     */
    fun isSpanish(context: Context): Boolean {
        return getCurrentLocale(context).language == "es"
    }
}
