package com.example.recyclerpractice.data.di

import android.app.Application
import androidx.room.Room
import com.example.recyclerpractice.data.local.NoteDao
import com.example.recyclerpractice.data.local.NoteDatabase
import com.example.recyclerpractice.data.repo.NotesRepositoryImpl
import com.example.recyclerpractice.domain.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            "notes_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: NoteDatabase): NoteDao = db.noteDao()

    @Provides
    @Singleton
    fun provideRepo(dao: NoteDao): NotesRepository {
        return NotesRepositoryImpl(dao)
    }
}