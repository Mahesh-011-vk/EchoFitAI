package com.echofit.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.echofit.app.data.local.EchoFitDatabase
import com.echofit.app.data.repository.EchoFitRepositoryImpl

class EchoFitApplication : Application() {

    // Database and Repository references for our clean DI service locator structure
    lateinit var database: EchoFitDatabase
        private set

    lateinit var repository: EchoFitRepositoryImpl
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize local Room DB
        database = EchoFitDatabase.getDatabase(this)

        // Initialize our central clean architecture repository implementation
        repository = EchoFitRepositoryImpl(
            userProfileDao = database.userProfileDao(),
            dailyLogDao = database.dailyLogDao(),
            dietWorkoutDao = database.dietWorkoutDao(),
            context = this
        )

        // Setup notification channel for daily fitness reminders
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "echofit_reminders"
            val channelName = "EchoFit Reminders"
            val channelDescription = "Alerts for meals, workouts, water intake, and rest periods"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        lateinit var instance: EchoFitApplication
            private set
    }
}
