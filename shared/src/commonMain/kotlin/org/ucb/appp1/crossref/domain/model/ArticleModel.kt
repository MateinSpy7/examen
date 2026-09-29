package org.ucb.appp1.crossref.domain.model

data class ArticleModel(
    val title: String,
    val authors: List<String>,
    val published: String,
    val containerTitle: String,
    val doi: String,
    val type: String,
    val url: String,
)
