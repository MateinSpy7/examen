package org.ucb.appp1.scheduler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.mp.KoinPlatform
import org.ucb.appp1.homeconfig.domain.usecase.SyncHomeConfigUseCase

class SyncHomeConfigWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        val useCase: SyncHomeConfigUseCase = KoinPlatform.getKoin().get()
        return if (useCase.invoke()) Result.success() else Result.failure()
    }
}
