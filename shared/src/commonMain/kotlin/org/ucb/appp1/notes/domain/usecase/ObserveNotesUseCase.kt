package org.ucb.appp1.notes.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.ucb.appp1.notes.domain.model.NoteModel
import org.ucb.appp1.notes.domain.repository.NoteRepository

class ObserveNotesUseCase(
    val repository: NoteRepository
) {
    fun invoke(): Flow<List<NoteModel>> {
        return repository.observe()
    }
}
