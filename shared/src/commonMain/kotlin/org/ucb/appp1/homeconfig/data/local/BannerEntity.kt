package org.ucb.appp1.homeconfig.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class BannerEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val position: Int
)
