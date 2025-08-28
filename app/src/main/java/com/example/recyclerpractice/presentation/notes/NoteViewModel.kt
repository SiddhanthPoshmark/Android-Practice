package com.example.recyclerpractice.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recyclerpractice.data.local.Note
import com.example.recyclerpractice.domain.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repo: NotesRepository): ViewModel() {

    val allNotes: Flow<List<Note>> = repo.allMessages
    fun insert(note : String) {
        viewModelScope.launch {
            repo.insert(note)
        }
    }

    fun incrementCount(noteId: Int) {
        viewModelScope.launch {
            repo.incrementCount(noteId)
        }
    }
}