package org.ucb.appp1.crossref.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CrossrefResponseDto(
    val message: CrossrefMessageDto? = null,
)

@Serializable
data class CrossrefMessageDto(
    val items: List<CrossrefWorkDto>? = null,
)

@Serializable
data class CrossrefWorkDto(
    val title: List<String>? = null,
    val author: List<CrossrefAuthorDto>? = null,
    val published: CrossrefDateDto? = null,
    @SerialName("container-title")
    val containerTitle: List<String>? = null,
    @SerialName("DOI")
    val doi: String? = null,
    val type: String? = null,
    @SerialName("URL")
    val url: String? = null,
)

@Serializable
data class CrossrefAuthorDto(
    val given: String? = null,
    val family: String? = null,
    val name: String? = null,
)

@Serializable
data class CrossrefDateDto(
    @SerialName("date-parts")
    val dateParts: List<List<Int?>>? = null,
)
