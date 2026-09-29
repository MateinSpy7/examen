package org.ucb.appp1.crossref.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ucb.appp1.crossref.domain.usecase.SearchArticlesUseCase

class CrossrefViewModel(
    val searchArticlesUseCase: SearchArticlesUseCase
): ViewModel() {
    private val _state = MutableStateFlow<CrossrefState>(CrossrefState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CrossrefEffect>()
    val effect = _effect.asSharedFlow()

    init {
        fetchArticles()
    }

    fun fetchArticles() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            searchArticlesUseCase.invoke(_state.value.query, ROWS).fold(
                onSuccess = { articles ->
                    _state.update {
                        it.copy(articles = articles, isLoading = false)
                    }
                },
                onFailure = { failure ->
                    _state.update {
                        it.copy(isLoading = false, error = failure.message ?: "Error de red o conexión")
                    }
                }
            )
        }
    }

    fun emitEvent(event: CrossrefEvent) {
        when(event) {
            CrossrefEvent.OnBack -> {
                emitEffect(CrossrefEffect.NavigateToBack)
            }
            else -> {}
        }
    }

    private fun emitEffect(effect: CrossrefEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private companion object {
        const val ROWS = 10
    }
}
