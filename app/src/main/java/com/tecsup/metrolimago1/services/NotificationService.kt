package com.tecsup.metrolimago1.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tecsup.metrolimago1.MainActivity
import com.tecsup.metrolimago1.R

class NotificationService(private val context: Context) {
    
    companion object {
        private const val CHANNEL_ID = "metrolima_alerts"
        private const val CHANNEL_NAME = "Alertas MetroLima"
        private const val CHANNEL_DESCRIPTION = "Notificaciones sobre el estado del Metro de Lima"
        
        // IDs de notificaciones
        private const val SERVICE_ALERT_ID = 1001
        private const val MAINTENANCE_ALERT_ID = 1002
        private const val DELAY_ALERT_ID = 1003
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    fun showServiceAlert(title: String, message: String, isUrgent: Boolean = false) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(if (isUrgent) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        
        if (isUrgent) {
            builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        }
        
        with(NotificationManagerCompat.from(context)) {
            notify(SERVICE_ALERT_ID, builder.build())
        }
    }
    
    fun showMaintenanceAlert(station: String, date: String, time: String) {
        val title = "Mantenimiento Programado"
        val message = "Mantenimiento en $station el $date de $time"
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        
        with(NotificationManagerCompat.from(context)) {
            notify(MAINTENANCE_ALERT_ID, builder.build())
        }
    }
    
    fun showDelayAlert(line: String, delay: String) {
        val title = "Retraso en $line"
        val message = "Se reporta un retraso de $delay en la $line"
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        
        with(NotificationManagerCompat.from(context)) {
            notify(DELAY_ALERT_ID, builder.build())
        }
    }
    
    fun showCustomAlert(title: String, message: String, priority: Int = NotificationCompat.PRIORITY_DEFAULT) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(priority)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        
        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}
