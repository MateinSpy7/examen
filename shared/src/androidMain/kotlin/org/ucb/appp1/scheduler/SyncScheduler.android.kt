package org.ucb.appp1.scheduler

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.koin.mp.KoinPlatform

actual class SyncScheduler {

    actual fun enqueueInitialSync() {
        enqueue(INITIAL_WORK_NAME, ExistingWorkPolicy.KEEP)
    }

    actual fun enqueueManualSync() {
        enqueue(MANUAL_WORK_NAME, ExistingWorkPolicy.REPLACE)
    }

    private fun enqueue(name: String, policy: ExistingWorkPolicy) {
        val context: Context = KoinPlatform.getKoin().get()
        val request = OneTimeWorkRequestBuilder<SyncHomeConfigWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(name, policy, request)
    }

    private companion object {
        const val INITIAL_WORK_NAME = "sync_home_config_initial"
        const val MANUAL_WORK_NAME = "sync_home_config_manual"
    }
}
