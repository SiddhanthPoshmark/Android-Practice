package com.example.recyclerpractice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recyclerpractice.db.NoteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repo: NotesRepository): ViewModel() {

    val allNotes: Flow<List<NoteEntity>> = repo.allMessages
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