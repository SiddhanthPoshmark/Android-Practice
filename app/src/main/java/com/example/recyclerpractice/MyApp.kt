package com.example.recyclerpractice

import android.app.Application
import com.example.recyclerpractice.db.NoteDatabase

class MyApp: Application() {

    lateinit var repo: NotesRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = NoteDatabase.getDatabase(this)
        repo = NotesRepository(db.noteDao())
    }
}