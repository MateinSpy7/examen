package org.ucb.appp1.homeconfig.data.local

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSHomeDirectory

actual class DatabaseFactory {
    actual fun create(): HomeConfigDatabase {
        return Room.databaseBuilder<HomeConfigDatabase>(
            name = NSHomeDirectory() + "/homeconfig.db"
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}
