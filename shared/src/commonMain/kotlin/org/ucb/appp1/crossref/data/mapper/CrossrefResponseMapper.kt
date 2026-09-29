package org.ucb.appp1.crossref.data.mapper

import org.ucb.appp1.crossref.data.dto.CrossrefAuthorDto
import org.ucb.appp1.crossref.data.dto.CrossrefDateDto
import org.ucb.appp1.crossref.data.dto.CrossrefResponseDto
import org.ucb.appp1.crossref.data.dto.CrossrefWorkDto
import org.ucb.appp1.crossref.domain.model.ArticleModel

fun CrossrefResponseDto.toDomain(): List<ArticleModel> =
    message?.items?.map { it.toDomain() } ?: emptyList()

fun CrossrefWorkDto.toDomain(): ArticleModel = ArticleModel(
    title = title?.firstOrNull() ?: "",
    authors = author?.map { it.toName() }?.filter { it.isNotBlank() } ?: emptyList(),
    published = published.toText(),
    containerTitle = containerTitle?.firstOrNull() ?: "",
    doi = doi ?: "",
    type = type ?: "",
    url = url ?: ""
)

private fun CrossrefAuthorDto.toName(): String =
    listOfNotNull(given, family).joinToString(" ").ifBlank { name ?: "" }

private fun CrossrefDateDto?.toText(): String =
    this?.dateParts?.firstOrNull()?.filterNotNull()?.joinToString("-") ?: ""
