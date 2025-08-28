package com.example.recyclerpractice.domain

import com.example.recyclerpractice.data.local.NoteDao
import com.example.recyclerpractice.data.local.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface NotesRepository {
    val allMessages: Flow<List<Note>>
    suspend fun insert(message: String)
    suspend fun incrementCount(noteId: Int)
}