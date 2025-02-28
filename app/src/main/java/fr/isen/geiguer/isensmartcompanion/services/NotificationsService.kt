package fr.isen.geiguer.isensmartcompanion.services

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.isen.geiguer.isensmartcompanion.models.EventModel
import fr.isen.geiguer.isensmartcompanion.R

class NotificationsService {
    fun scheduleNotification(context: Context, event: EventModel) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("event_title", event.title)
            putExtra("event_description", event.description)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event.title.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 10000,
            pendingIntent
        )
    }

    class NotificationReceiver : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context, intent: Intent) {
            val title = intent.getStringExtra("event_title")
            val description = intent.getStringExtra("event_description")

            val channelId = "event_notifications"
            val channelName = "Event Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.la_mere_patriev3)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            NotificationManagerCompat.from(context).notify(title.hashCode(), notification)
        }
    }

}