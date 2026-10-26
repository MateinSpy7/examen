package org.ucb.appp1.homeconfig.presentation.viewmodel

import org.ucb.appp1.homeconfig.domain.model.BannerModel
import org.ucb.appp1.homeconfig.domain.model.FeatureModel

data class HomeConfigState(
    val features: List<FeatureModel> = emptyList(),
    val banners: List<BannerModel> = emptyList(),
    val isCached: Boolean = true,
    val isRefreshing: Boolean = false
) {
    val hasData: Boolean get() = features.isNotEmpty() || banners.isNotEmpty()
}
