package com.echofit.app.data.receiver

import android.content.Context
import androidx.work.*
import com.echofit.app.domain.model.ReminderSetting
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    /**
     * Set up or cancel a WorkManager task for the given reminder configuration.
     */
    fun scheduleReminder(context: Context, reminder: ReminderSetting) {
        val workManager = WorkManager.getInstance(context)
        val tag = "reminder_${reminder.id}"

        // Always cancel existing matching alerts before rescheduled updates
        workManager.cancelAllWorkByTag(tag)

        if (!reminder.isEnabled) return

        val initialDelayMin = calculateInitialDelayMinutes(reminder.time)

        val inputData = Data.Builder()
            .putString("reminder_id", reminder.id)
            .putString("reminder_name", reminder.name)
            .build()

        // Create periodic daily work using the initial offset calculation
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelayMin, TimeUnit.MINUTES)
            .setInputData(inputData)
            .addTag(tag)
            .build()

        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    private fun calculateInitialDelayMinutes(timeStr: String): Long {
        try {
            val sdf = SimpleDateFormat("hh:mm a", Locale.US)
            val parsedDate = sdf.parse(timeStr) ?: return 5L
            val targetCal = Calendar.getInstance().apply {
                val parsedCal = Calendar.getInstance().apply { time = parsedDate }
                set(Calendar.HOUR_OF_DAY, parsedCal.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, parsedCal.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val now = Calendar.getInstance()
            if (targetCal.before(now)) {
                // If time already passed today, advance offset target to tomorrow
                targetCal.add(Calendar.DAY_OF_YEAR, 1)
            }

            val diffMs = targetCal.timeInMillis - now.timeInMillis
            return TimeUnit.MILLISECONDS.toMinutes(diffMs).coerceAtLeast(1L)
        } catch (e: Exception) {
            e.printStackTrace()
            return 10L // Safe small delay backup
        }
    }
}
