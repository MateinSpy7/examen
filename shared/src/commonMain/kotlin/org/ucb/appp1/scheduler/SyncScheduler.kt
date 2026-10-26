package org.ucb.appp1.scheduler

expect class SyncScheduler() {
    fun enqueueInitialSync()
    fun enqueueManualSync()
}
