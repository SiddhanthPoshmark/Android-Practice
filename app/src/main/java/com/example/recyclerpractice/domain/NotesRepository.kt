package com.example.recyclerpractice.domain

import com.example.recyclerpractice.data.local.NoteDao
import com.example.recyclerpractice.data.local.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepository @Inject constructor(private val dao: NoteDao) {
    val allMessages: Flow<List<Note>> = dao.getAllNotes()

    suspend fun insert(message: String) {
        dao.insert(Note(note = message))
    }

    suspend fun incrementCount(noteId: Int) {
        dao.incrementCount(noteId)
    }
}