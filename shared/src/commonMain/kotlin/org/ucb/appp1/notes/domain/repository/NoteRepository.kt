package org.ucb.appp1.notes.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ucb.appp1.notes.domain.model.NoteModel

interface NoteRepository {
    fun observe(): Flow<List<NoteModel>>
    suspend fun add(title: String, description: String)
    suspend fun delete(id: String)
}
