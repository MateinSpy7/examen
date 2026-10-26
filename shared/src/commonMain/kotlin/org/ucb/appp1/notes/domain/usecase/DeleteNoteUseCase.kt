package org.ucb.appp1.notes.domain.usecase

import org.ucb.appp1.notes.domain.repository.NoteRepository

class DeleteNoteUseCase(
    val repository: NoteRepository
) {
    suspend fun invoke(id: String) {
        repository.delete(id)
    }
}
