package org.ucb.appp1.crossref.presentation.viewmodel

import org.ucb.appp1.crossref.domain.model.ArticleModel

data class CrossrefState(
    val query: String = "machine learning",
    val isLoading: Boolean = false,
    val error: String? = null,
    val articles: List<ArticleModel> = emptyList()
)
