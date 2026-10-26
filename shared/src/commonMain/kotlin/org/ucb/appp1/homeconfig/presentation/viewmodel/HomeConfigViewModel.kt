package org.ucb.appp1.homeconfig.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ucb.appp1.homeconfig.domain.usecase.ObserveHomeConfigUseCase
import org.ucb.appp1.scheduler.SyncScheduler
import kotlin.time.Clock

class HomeConfigViewModel(
    val usecase: ObserveHomeConfigUseCase,
    val scheduler: SyncScheduler
): ViewModel() {

    private val _state = MutableStateFlow(HomeConfigState())
    val state = _state.asStateFlow()

    private var refreshStartedAt = 0L

    init {
        observeRoom()
    }

    // La UI solo lee de Room: cada cambio en la base se refleja aqui
    private fun observeRoom() {
        viewModelScope.launch {
            usecase.invoke().collect { config ->
                _state.update { current ->
                    current.copy(
                        features = config.features,
                        banners = config.banners,
                        isCached = config.isCached,
                        isRefreshing = current.isRefreshing && config.lastAttemptAt < refreshStartedAt
                    )
                }
            }
        }
    }

    fun refresh() {
        refreshStartedAt = Clock.System.now().toEpochMilliseconds()
        _state.update { it.copy(isRefreshing = true) }
        scheduler.enqueueManualSync()
        viewModelScope.launch {
            delay(REFRESH_TIMEOUT_MS)
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private companion object {
        const val REFRESH_TIMEOUT_MS = 15_000L
    }
}
