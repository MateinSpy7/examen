package org.ucb.appp1.scheduler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import org.ucb.appp1.homeconfig.domain.usecase.SyncHomeConfigUseCase

actual class SyncScheduler {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    actual fun enqueueInitialSync() {
        sync()
    }

    actual fun enqueueManualSync() {
        sync()
    }

    private fun sync() {
        scope.launch {
            KoinPlatform.getKoin().get<SyncHomeConfigUseCase>().invoke()
        }
    }
}
