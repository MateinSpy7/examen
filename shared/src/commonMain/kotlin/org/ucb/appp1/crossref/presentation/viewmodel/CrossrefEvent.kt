package org.ucb.appp1.crossref.presentation.viewmodel

sealed interface CrossrefEvent {
    object OnBack : CrossrefEvent
    object OnSubmit: CrossrefEvent
    data class OnQueryChange(val value: String): CrossrefEvent
}
