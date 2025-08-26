package com.example.recyclerpractice

import com.example.recyclerpractice.db.NoteDao
import com.example.recyclerpractice.db.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepository @Inject constructor(private val dao: NoteDao) {
    val allMessages: Flow<List<NoteEntity>> = dao.getAllNotes()

    suspend fun insert(message: String) {
        dao.insert(NoteEntity(note = message))
    }

    suspend fun incrementCount(noteId: Int) {
        dao.incrementCount(noteId)
    }
}