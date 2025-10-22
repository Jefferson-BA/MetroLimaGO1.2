package com.tecsup.metrolimago1.viewmodels

import android.app.Application
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.metrolimago1.services.NotificationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.isActive

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    
    private val notificationService = NotificationService(application)
    private var isMonitoring = false
    
    fun startMonitoring() {
        if (isMonitoring) return
        isMonitoring = true
        
        viewModelScope.launch {
            while (isActive && isMonitoring) {
                // Simular alertas aleatorias cada 30-60 segundos
                val delayTime = (30000..60000).random()
                delay(delayTime)
                
                if (isActive) {
                    simulateRandomAlert()
                }
            }
        }
    }
    
    fun stopMonitoring() {
        isMonitoring = false
    }
    
    private fun simulateRandomAlert() {
        val alerts = listOf(
            Triple("Servicio Normal", "Todas las líneas operando con normalidad", false),
            Triple("Retraso Menor", "Línea 1 con retraso de 5 minutos", true),
            Triple("Mantenimiento", "Mantenimiento programado en estación Grau", false),
            Triple("Servicio Restaurado", "Línea 2 ha restaurado su servicio normal", false),
            Triple("Retraso Mayor", "Línea 1 con retraso de 15 minutos", true)
        )
        
        val randomAlert = alerts.random()
        notificationService.showServiceAlert(
            randomAlert.first,
            randomAlert.second,
            randomAlert.third
        )
    }
    
    fun sendMaintenanceAlert(station: String, date: String, time: String) {
        notificationService.showMaintenanceAlert(station, date, time)
    }
    
    fun sendDelayAlert(line: String, delay: String) {
        notificationService.showDelayAlert(line, delay)
    }
    
    fun sendCustomAlert(title: String, message: String, isUrgent: Boolean = false) {
        notificationService.showCustomAlert(
            title,
            message,
            if (isUrgent) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT
        )
    }
}
