package org.ucb.appp1.crossref.domain.usecase

import org.ucb.appp1.crossref.domain.model.ArticleModel
import org.ucb.appp1.crossref.domain.repository.CrossrefRepository

class SearchArticlesUseCase(
    val repository: CrossrefRepository
) {
    suspend fun invoke(query: String, rows: Int): Result<List<ArticleModel>> {
        return repository.searchArticles(query, rows)
    }
}
