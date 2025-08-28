package com.example.recyclerpractice.data.repo

import com.example.recyclerpractice.data.local.Note
import com.example.recyclerpractice.data.local.NoteDao
import com.example.recyclerpractice.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(private val dao: NoteDao): NotesRepository {
    override val allMessages: Flow<List<Note>> = dao.getAllNotes()

    override suspend fun insert(message: String) {
        dao.insert(Note(note = message))
    }

    override suspend fun incrementCount(noteId: Int) {
        dao.incrementCount(noteId)
    }
}