package com.example.recyclerpractice

import com.example.recyclerpractice.db.NoteDao
import com.example.recyclerpractice.db.NoteEntity
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val dao: NoteDao) {
    val allMessages: Flow<List<NoteEntity>> = dao.getAllNotes()

    suspend fun insert(message: String) {
        dao.insert(NoteEntity(note = message))
    }

    suspend fun incrementCount(noteId: Int) {
        dao.incrementCount(noteId)
    }
}