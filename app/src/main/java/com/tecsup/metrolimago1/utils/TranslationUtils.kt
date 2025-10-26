package com.tecsup.metrolimago1.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

object TranslationUtils {
    
    /**
     * Cambia el idioma de la aplicación
     * @param context Contexto de la aplicación
     * @param language Código del idioma ("es" o "en")
     * @return Contexto actualizado
     */
    fun changeLanguage(context: Context, language: String): Context {
        // Guardar preferencia de idioma
        val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("selected_language", language).apply()
        
        val locale = when (language) {
            "es" -> Locale("es", "ES")
            "en" -> Locale("en", "US")
            else -> Locale.getDefault()
        }
        
        // Aplicar el cambio de idioma
        Locale.setDefault(locale)
        val newContext = LocaleUtils.updateLocale(context, locale)
        
        // Forzar actualización de recursos
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        
        return newContext
    }
    
    /**
     * Obtiene el idioma guardado en preferencias
     * @param context Contexto de la aplicación
     * @return Código del idioma guardado o "es" por defecto
     */
    fun getSavedLanguage(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getString("selected_language", "es") ?: "es"
    }
    
    /**
     * Aplica el idioma guardado al contexto de la aplicación
     * @param context Contexto de la aplicación
     */
    fun applySavedLanguage(context: Context) {
        val savedLanguage = getSavedLanguage(context)
        val locale = when (savedLanguage) {
            "es" -> Locale("es", "ES")
            "en" -> Locale("en", "US")
            else -> Locale.getDefault()
        }
        Locale.setDefault(locale)
        LocaleUtils.updateLocale(context, locale)
    }
    
    /**
     * Función centralizada para obtener traducciones
     * @param context Contexto de la aplicación
     * @param key Clave de la traducción
     * @return Texto traducido
     */
    fun getText(context: Context, key: String): String {
        // Obtener idioma guardado
        val savedLanguage = getSavedLanguage(context)
        val isSpanish = savedLanguage == "es"
        
        return when (key) {
            // Navegación y botones
            "back" -> if (isSpanish) "Atrás" else "Back"
            "next" -> if (isSpanish) "Siguiente" else "Next"
            "continue" -> if (isSpanish) "Continuar" else "Continue"
            "cancel" -> if (isSpanish) "Cancelar" else "Cancel"
            "save" -> if (isSpanish) "Guardar" else "Save"
            "delete" -> if (isSpanish) "Eliminar" else "Delete"
            "edit" -> if (isSpanish) "Editar" else "Edit"
            "close" -> if (isSpanish) "Cerrar" else "Close"
            "open" -> if (isSpanish) "Abrir" else "Open"
            "ok" -> if (isSpanish) "Aceptar" else "OK"
            "yes" -> if (isSpanish) "Sí" else "Yes"
            "no" -> if (isSpanish) "No" else "No"
            "confirm" -> if (isSpanish) "Confirmar" else "Confirm"
            "retry" -> if (isSpanish) "Reintentar" else "Retry"
            "refresh" -> if (isSpanish) "Actualizar" else "Refresh"
            
            // Estados del servicio
            "operational" -> if (isSpanish) "Operativo" else "Operational"
            "maintenance" -> if (isSpanish) "Mantenimiento" else "Maintenance"
            "scheduled_maintenance" -> if (isSpanish) "Mantenimiento Programado" else "Scheduled Maintenance"
            "service_interruption" -> if (isSpanish) "Interrupción del Servicio" else "Service Interruption"
            
            // Navegación de la app
            "home" -> if (isSpanish) "Inicio" else "Home"
            "routes" -> if (isSpanish) "Rutas" else "Routes"
            "stations" -> if (isSpanish) "Estaciones" else "Stations"
            "live" -> if (isSpanish) "En Vivo" else "Live"
            "settings" -> if (isSpanish) "Configuración" else "Settings"
            "chat" -> if (isSpanish) "Chat" else "Chat"
            
            // Configuración
            "appearance" -> if (isSpanish) "Apariencia" else "Appearance"
            "language" -> if (isSpanish) "Idioma" else "Language"
            "dark_mode" -> if (isSpanish) "Modo Oscuro" else "Dark Mode"
            "change_theme" -> if (isSpanish) "Cambia el tema de la aplicación" else "Change the application theme"
            "select_language" -> if (isSpanish) "Selecciona tu idioma preferido" else "Select your preferred language"
            "contact" -> if (isSpanish) "Contacto" else "Contact"
            "developer" -> if (isSpanish) "Desarrollador" else "Developer"
            
            // Búsqueda y navegación
            "search" -> if (isSpanish) "Buscar" else "Search"
            "notifications" -> if (isSpanish) "Notificaciones" else "Notifications"
            "clock" -> if (isSpanish) "Reloj" else "Clock"
            
            // Chat y asistente
            "ai_assistant" -> if (isSpanish) "Asistente IA" else "AI Assistant"
            "type_message" -> if (isSpanish) "Escribe tu mensaje..." else "Type your message..."
            "send" -> if (isSpanish) "Enviar" else "Send"
            
            // Mapas y rutas
            "see_route" -> if (isSpanish) "Ver Ruta" else "See Route"
            "clear_route" -> if (isSpanish) "Limpiar Ruta" else "Clear Route"
            "activate_map" -> if (isSpanish) "Activar Mapa" else "Activate Map"
            "places" -> if (isSpanish) "Lugares" else "Places"
            "all" -> if (isSpanish) "Todas" else "All"
            "distance" -> if (isSpanish) "Distancia" else "Distance"
            "time" -> if (isSpanish) "Tiempo" else "Time"
            "steps" -> if (isSpanish) "Pasos" else "Steps"
            
            // Mensajes comunes
            "loading" -> if (isSpanish) "Cargando..." else "Loading..."
            "error" -> if (isSpanish) "Error" else "Error"
            "success" -> if (isSpanish) "Éxito" else "Success"
            "warning" -> if (isSpanish) "Advertencia" else "Warning"
            
            // Mensajes de cambio de idioma
            "language_change_message" -> if (isSpanish) "El idioma se ha cambiado correctamente." else "Language has been changed successfully."
            "language_change_question" -> if (isSpanish) "¿Deseas reiniciar la aplicación ahora para aplicar los cambios?" else "Do you want to restart the application now to apply the changes?"
            "language_restart_now" -> if (isSpanish) "Reiniciar Ahora" else "Restart Now"
            "language_restart_later" -> if (isSpanish) "Reiniciar Más Tarde" else "Restart Later"
            
            // Textos adicionales de configuración
            "about" -> if (isSpanish) "Acerca de" else "About"
            "version" -> if (isSpanish) "Versión" else "Version"
            "app_description" -> if (isSpanish) "MetroLima GO es tu compañero ideal para navegar por el sistema de Metro de Lima. Planifica tus viajes, consulta horarios y encuentra la mejor ruta." else "MetroLima GO is your ideal companion for navigating Lima's Metro system. Plan your trips, check schedules and find the best route."
            "development_team" -> if (isSpanish) "Equipo de Desarrollo MetroLima" else "MetroLima Development Team"
            "spanish" -> if (isSpanish) "Español" else "Spanish"
            "english" -> if (isSpanish) "Inglés" else "English"
            
            // Textos adicionales para cards
            "dark_mode_description" -> if (isSpanish) "Cambia el tema de la aplicación" else "Change the application theme"
            "language_description" -> if (isSpanish) "Selecciona tu idioma preferido" else "Select your preferred language"
            "app_version" -> if (isSpanish) "Versión" else "Version"
            "app_name" -> if (isSpanish) "MetroLima GO" else "MetroLima GO"
            "app_team" -> if (isSpanish) "Equipo de Desarrollo MetroLima" else "MetroLima Development Team"
            "app_contact" -> if (isSpanish) "Contacto" else "Contact"
            "app_developer" -> if (isSpanish) "Desarrollador" else "Developer"
            
            // Por defecto
            else -> key
        }
    }
}