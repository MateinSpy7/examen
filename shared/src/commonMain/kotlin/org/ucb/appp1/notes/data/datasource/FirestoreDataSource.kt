package org.ucb.appp1.notes.data.datasource

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.ucb.appp1.notes.data.dto.NoteDto
import org.ucb.appp1.notes.domain.model.NoteModel

class FirestoreDataSource {

    private val notesRef by lazy { Firebase.firestore.collection(COLLECTION) }

    fun observeNotes(): Flow<List<NoteModel>> {
        return notesRef.snapshots.map { snapshot ->
            snapshot.documents.map { document ->
                val dto = document.data<NoteDto>()
                NoteModel(id = document.id, title = dto.title, description = dto.description)
            }
        }
    }

    suspend fun addNote(title: String, description: String) {
        notesRef.add(NoteDto(title = title, description = description))
    }

    suspend fun deleteNote(id: String) {
        notesRef.document(id).delete()
    }

    private companion object {
        const val COLLECTION = "notes"
    }
}
