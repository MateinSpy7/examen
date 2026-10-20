package org.ucb.appp1.exchange.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ucb.appp1.exchange.domain.model.ContactInfo
import org.ucb.appp1.exchange.domain.usecase.GetContactInfoUseCase
import org.ucb.appp1.exchange.domain.usecase.ObserveExchangeUseCase

class ExchangeViewModel(
    val usecase: ObserveExchangeUseCase,
    val contactUseCase: GetContactInfoUseCase
): ViewModel() {

    private val _message = MutableStateFlow<String>("")
    val message = _message.asStateFlow()

    private val _contact = MutableStateFlow<ContactInfo?>(null)
    val contact = _contact.asStateFlow()

    init {
        observeMessage()
        loadContact()
    }

    private fun loadContact() {
        viewModelScope.launch {
            _contact.value = try {
                contactUseCase.invoke()
            } catch (e: Exception) {
                ContactInfo(phone = "Error: ${e.message}", email = "Error: ${e.message}")
            }
        }
    }

    private fun observeMessage() {
        viewModelScope.launch {
            usecase.invoke()
                .collect { message ->
                    _message.value = message?:"Test"
                }
        }
    }

}
