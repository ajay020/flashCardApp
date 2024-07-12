package com.example.flashcard.data

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.flashcard.utils.REMINDER_WORKER_TAG
import com.example.flashcard.utils.REMINDER_WORK_NAME
import com.example.flashcard.workers.ReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class WorkManagerReminderRepository(context: Context) : ReminderRepository {

    private val workManager = WorkManager.getInstance(context)

    /**
     * Cancel any ongoing WorkRequests
     * */
    override fun cancelWork() {
        workManager.cancelAllWorkByTag(REMINDER_WORKER_TAG)
    }

    override fun scheduleDailyReminder() {
        scheduleReminder()
    }

    private fun scheduleReminder() {
        val currentTime = Calendar.getInstance()
        val reminderTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
        }

        if (reminderTime.before(currentTime)) {
            reminderTime.add(Calendar.DAY_OF_YEAR, 1)
        }

        val initialDelay = reminderTime.timeInMillis - currentTime.timeInMillis

        val dailyWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWorkRequest
        )
    }

    private fun scheduleReminderByTime(timeInMillis: Long, context: Context) {
        val delay = timeInMillis - System.currentTimeMillis()
        val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(4, TimeUnit.SECONDS)
            .addTag("REMINDER_TAG")
            .build()

        WorkManager.getInstance(context).enqueue(reminderRequest)
    }

}