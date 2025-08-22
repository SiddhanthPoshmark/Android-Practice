package com.example.recyclerpractice.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun insert(note: NoteEntity)

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("UPDATE notes SET count = count + 1 WHERE id = :noteId")
    suspend fun incrementCount(noteId: Int)

}