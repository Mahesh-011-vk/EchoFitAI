package com.echofit.app.data.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.echofit.app.MainActivity

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val reminderId = inputData.getString("reminder_id") ?: "general"
        val reminderName = inputData.getString("reminder_name") ?: "Daily Fitness checkin"

        sendReminderNotification(reminderId, reminderName)
        return Result.success()
    }

    private fun sendReminderNotification(id: String, name: String) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "echofit_reminders"
        
        // Ensure channel exists (just in case)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "EchoFit Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Alerts for meals, workouts, water intake, and rest periods"
            }
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("EchoFit daily coach")
            .setContentText("Hey! It's time for your $name! Tap to log with Echo.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            val notificationManager = NotificationManagerCompat.from(applicationContext)
            notificationManager.notify(id.hashCode(), builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
