package com.echofit.app.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.PendingIntent
import com.echofit.app.MainActivity

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getStringExtra("reminder_id") ?: "general"
        val reminderName = intent.getStringExtra("reminder_name") ?: "Fitness checkin!"

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            reminderId.hashCode(),
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "echofit_reminders")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("EchoFit Daily Checkin")
            .setContentText("It's time for your $reminderName! Tap to log with Echo.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            // Permission checked in AndroidManifest.xml
            notificationManager.notify(reminderId.hashCode(), builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
