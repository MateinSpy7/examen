package org.ucb.appp1.homeconfig.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [FeatureEntity::class, BannerEntity::class, SyncStatusEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(HomeConfigDatabaseConstructor::class)
abstract class HomeConfigDatabase : RoomDatabase() {
    abstract fun homeConfigDao(): HomeConfigDao
}

@Suppress("KotlinNoActualForExpect")
expect object HomeConfigDatabaseConstructor : RoomDatabaseConstructor<HomeConfigDatabase> {
    override fun initialize(): HomeConfigDatabase
}
