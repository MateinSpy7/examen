package org.ucb.appp1

import org.ucb.appp1.di.initKoin
import org.ucb.appp1.scheduler.SyncScheduler

fun initKoinIos() {
    initKoin ()
    SyncScheduler().enqueueInitialSync()
}
