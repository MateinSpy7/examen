package org.ucb.appp1.homeconfig.domain.model

data class HomeConfigModel(
    val features: List<FeatureModel> = emptyList(),
    val banners: List<BannerModel> = emptyList(),
    val isCached: Boolean = true,
    val lastAttemptAt: Long = 0L
)
