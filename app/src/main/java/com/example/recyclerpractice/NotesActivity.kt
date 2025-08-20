package com.example.recyclerpractice

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerpractice.db.NoteDatabase
import kotlinx.coroutines.launch

class NotesActivity : AppCompatActivity() {

    private lateinit var adapter: NotesAdapter
    private lateinit var viewModel: NoteViewModel

    class NotesViewModelFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NoteViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        val db = NoteDatabase.getDatabase(this)
        val repository = NotesRepository(db.noteDao())
        viewModel = ViewModelProvider(this, NotesViewModelFactory(repository))[NoteViewModel::class.java]

        adapter = NotesAdapter { note ->
            viewModel.incrementCount(note.id)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val editNote = findViewById<EditText>(R.id.editNote)
        val buttonAdd = findViewById<Button>(R.id.addButton)

        buttonAdd.setOnClickListener {
            val note = editNote.text.toString()
            if (note.isNotEmpty()) {
                viewModel.insert(note)
                editNote.text.clear()
            }
        }
        var lastListSize = 0
        lifecycleScope.launch {
            viewModel.allNotes.collect { notes ->
                val isNewItemAdded = notes.size > lastListSize
                lastListSize = notes.size

                adapter.submitList(notes) {
                    if (isNewItemAdded) {
                        recyclerView.smoothScrollToPosition(0)
                    }
                }
            }
        }
    }
}

