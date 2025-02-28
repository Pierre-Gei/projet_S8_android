package fr.isen.geiguer.isensmartcompanion.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import fr.isen.geiguer.isensmartcompanion.R
import fr.isen.geiguer.isensmartcompanion.models.EventModel

class NotificationsService {
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Notifications"
            val descriptionText = "Notifications for events"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("event_notification", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(context: Context, event: EventModel) {
        val notificationBuilder = NotificationCompat.Builder(context, "event_notification")
            .setSmallIcon(R.drawable.la_mere_patriev3)
            .setContentTitle(event.title)
            .setContentText(event.description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            notificationManager.notify(event.hashCode(), notificationBuilder.build())
        }, 10000)
    }
}