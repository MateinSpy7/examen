package org.ucb.appp1.notes.data.repository

import kotlinx.coroutines.flow.Flow
import org.ucb.appp1.notes.data.datasource.FirestoreDataSource
import org.ucb.appp1.notes.domain.model.NoteModel
import org.ucb.appp1.notes.domain.repository.NoteRepository

class NoteRepositoryImpl(
    val dataSource: FirestoreDataSource
): NoteRepository {
    override fun observe(): Flow<List<NoteModel>> {
        return dataSource.observeNotes()
    }

    override suspend fun add(title: String, description: String) {
        dataSource.addNote(title, description)
    }

    override suspend fun delete(id: String) {
        dataSource.deleteNote(id)
    }
}
