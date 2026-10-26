package org.ucb.appp1.homeconfig.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class HomeConfigDao {

    @Query("SELECT * FROM features")
    abstract fun observeFeatures(): Flow<List<FeatureEntity>>

    @Query("SELECT * FROM banners ORDER BY position")
    abstract fun observeBanners(): Flow<List<BannerEntity>>

    @Query("SELECT * FROM sync_status WHERE id = 0")
    abstract fun observeStatus(): Flow<SyncStatusEntity?>

    @Query("DELETE FROM features")
    abstract suspend fun clearFeatures()

    @Query("DELETE FROM banners")
    abstract suspend fun clearBanners()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFeatures(features: List<FeatureEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBanners(banners: List<BannerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveStatus(status: SyncStatusEntity)

    @Transaction
    open suspend fun replaceAll(
        features: List<FeatureEntity>,
        banners: List<BannerEntity>,
        status: SyncStatusEntity
    ) {
        clearFeatures()
        clearBanners()
        insertFeatures(features)
        insertBanners(banners)
        saveStatus(status)
    }
}
