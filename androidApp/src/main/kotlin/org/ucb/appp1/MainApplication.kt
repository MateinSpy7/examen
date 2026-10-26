package org.ucb.appp1

import android.app.Application
import org.ucb.appp1.di.initKoinAndroid
import org.ucb.appp1.scheduler.LogScheduler
import org.ucb.appp1.scheduler.SyncScheduler

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
        SyncScheduler().enqueueInitialSync()
        LogScheduler().schedulerPeriodicalUpload()
    }
}