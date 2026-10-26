package org.ucb.appp1.notifications.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.FirebaseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ucb.appp1.notifications.domain.usecase.GetFcmTokenUseCase

class NotificationsViewModel(
    val usecase: GetFcmTokenUseCase
): ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()

    init {
        loadToken()
    }

    private fun loadToken() {
        viewModelScope.launch {
            _state.value = try {
                NotificationsState(isLoading = false, token = usecase.invoke())
            } catch (e: FirebaseException) {
                NotificationsState(isLoading = false, error = e.message ?: "Error al obtener el token")
            }
        }
    }
}
