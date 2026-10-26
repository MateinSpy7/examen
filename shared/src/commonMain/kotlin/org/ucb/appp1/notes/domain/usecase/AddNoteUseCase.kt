package org.ucb.appp1.notes.domain.usecase

import org.ucb.appp1.notes.domain.repository.NoteRepository

class AddNoteUseCase(
    val repository: NoteRepository
) {
    suspend fun invoke(title: String, description: String) {
        repository.add(title, description)
    }
}
