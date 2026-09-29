package org.ucb.appp1.crossref.domain.repository

import org.ucb.appp1.crossref.domain.model.ArticleModel

interface CrossrefRepository {
    suspend fun searchArticles(query: String, rows: Int): Result<List<ArticleModel>>
}
