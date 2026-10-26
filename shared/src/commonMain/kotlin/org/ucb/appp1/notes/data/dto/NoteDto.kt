package org.ucb.appp1.notes.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val title: String = "",
    val description: String = ""
)
