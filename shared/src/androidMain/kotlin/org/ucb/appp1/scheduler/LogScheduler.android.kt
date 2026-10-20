package org.ucb.appp1.scheduler

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import org.koin.mp.KoinPlatform
import java.util.concurrent.TimeUnit

actual class LogScheduler {
    actual fun schedulerPeriodicalUpload() {
        val context: Context = KoinPlatform.getKoin().get()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<LogUploadWorker>(REPEAT_INTERVAL_MINUTES, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private companion object {
        const val UNIQUE_WORK_NAME = "log_upload_work"
        const val REPEAT_INTERVAL_MINUTES = 15L
    }
}
