package org.ucb.appp1.homeconfig.data.local

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.mp.KoinPlatform

actual class DatabaseFactory {
    actual fun create(): HomeConfigDatabase {
        val context: Context = KoinPlatform.getKoin().get()
        return Room.databaseBuilder<HomeConfigDatabase>(
            context = context,
            name = context.getDatabasePath(DATABASE_NAME).absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    private companion object {
        const val DATABASE_NAME = "homeconfig.db"
    }
}
