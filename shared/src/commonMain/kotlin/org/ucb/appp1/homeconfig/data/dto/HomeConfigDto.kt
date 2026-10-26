package org.ucb.appp1.homeconfig.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class HomeConfigDto(
    val features: List<FeatureDto> = emptyList(),
    val banners: List<BannerDto> = emptyList()
)

@Serializable
data class FeatureDto(
    val id: String,
    val title: String,
    val description: String = "",
    val enabled: Boolean = true
)

@Serializable
data class BannerDto(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val imageUrl: String = "",
    val position: Int = 0
)
