package com.tecsup.metrolimago1.utils

import android.content.Context
import java.io.IOException
import java.util.Properties

object ConfigManager {
    private var properties: Properties? = null

    fun initialize(context: Context) {
        try {
            properties = Properties()
            val inputStream = context.assets.open("config.properties")
            properties?.load(inputStream)
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getGoogleMapsApiKey(): String {
        return properties?.getProperty("GOOGLE_MAPS_API_KEY", "") ?: ""
    }

    // La misma API key sirve para todas las APIs de Google Maps Platform
    fun getGoogleDirectionsApiKey(): String {
        return getGoogleMapsApiKey()
    }

    fun getGooglePlacesApiKey(): String {
        return getGoogleMapsApiKey()
    }

    fun getMetroLimaApiKey(): String {
        return properties?.getProperty("METRO_LIMA_API_KEY", "") ?: ""
    }

    fun getMetroLimaBaseUrl(): String {
        // Si no hay config.properties, usar URL por defecto para desarrollo local
        return properties?.getProperty("METRO_LIMA_BASE_URL", "http://10.0.2.2:8000/api/") 
            ?: "http://10.0.2.2:8000/api/" // Para emulador Android
    }

    fun isDebugMaps(): Boolean {
        return properties?.getProperty("DEBUG_MAPS", "false")?.toBoolean() ?: false
    }

    fun getLimaCenterLat(): Double {
        return properties?.getProperty("LIMA_CENTER_LAT", "-12.0464")?.toDouble() ?: -12.0464
    }

    fun getLimaCenterLng(): Double {
        return properties?.getProperty("LIMA_CENTER_LNG", "-77.0428")?.toDouble() ?: -77.0428
    }

    fun getLimaZoomLevel(): Float {
        return properties?.getProperty("LIMA_ZOOM_LEVEL", "12")?.toFloat() ?: 12f
    }
}
