package org.ucb.appp1.homeconfig.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_status")
data class SyncStatusEntity(
    @PrimaryKey val id: Int = STATUS_ID,
    val lastAttemptAt: Long,
    val lastSyncSuccess: Boolean,
    val errorMessage: String? = null
) {
    companion object {
        const val STATUS_ID = 0
    }
}
