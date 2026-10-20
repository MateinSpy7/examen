package org.ucb.appp1.scheduler

actual class LogScheduler {
    actual fun schedulerPeriodicalUpload() {
        // No-op en iOS: las tareas en segundo plano solo se implementan en Android.
    }
}
