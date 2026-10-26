package org.ucb.appp1.homeconfig.data.mapper

import org.ucb.appp1.homeconfig.data.dto.BannerDto
import org.ucb.appp1.homeconfig.data.dto.FeatureDto
import org.ucb.appp1.homeconfig.data.local.BannerEntity
import org.ucb.appp1.homeconfig.data.local.FeatureEntity
import org.ucb.appp1.homeconfig.domain.model.BannerModel
import org.ucb.appp1.homeconfig.domain.model.FeatureModel

fun FeatureDto.toEntity() = FeatureEntity(
    id = id,
    title = title,
    description = description,
    enabled = enabled
)

fun BannerDto.toEntity() = BannerEntity(
    id = id,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    position = position
)

fun FeatureEntity.toModel() = FeatureModel(
    id = id,
    title = title,
    description = description,
    enabled = enabled
)

fun BannerEntity.toModel() = BannerModel(
    id = id,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl
)
