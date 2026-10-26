package org.ucb.appp1.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dev.gitlive.firebase.FirebaseException
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ucb.appp1.notes.domain.model.NoteModel
import org.ucb.appp1.notes.domain.usecase.AddNoteUseCase
import org.ucb.appp1.notes.domain.usecase.DeleteNoteUseCase
import org.ucb.appp1.notes.domain.usecase.ObserveNotesUseCase

class NotesViewModel(
    val observeNotes: ObserveNotesUseCase,
    val addNote: AddNoteUseCase,
    val deleteNote: DeleteNoteUseCase
): ViewModel() {

    private val _notes = MutableStateFlow<List<NoteModel>>(emptyList())
    val notes = _notes.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            try {
                observeNotes.invoke().collect { _notes.value = it }
            } catch (e: FirebaseException) {
                _error.value = e.message
            }
        }
    }

    fun add(title: String, description: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            try {
                addNote.invoke(title.trim(), description.trim())
            } catch (e: FirebaseException) {
                _error.value = e.message
            }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            try {
                deleteNote.invoke(id)
            } catch (e: FirebaseException) {
                _error.value = e.message
            }
        }
    }
}
