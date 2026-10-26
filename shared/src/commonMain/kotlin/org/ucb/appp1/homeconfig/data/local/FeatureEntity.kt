package org.ucb.appp1.homeconfig.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "features")
data class FeatureEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val enabled: Boolean
)
