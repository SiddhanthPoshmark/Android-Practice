package com.example.recyclerpractice.presentation.notes

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerpractice.R
import kotlinx.coroutines.launch

class NotesActivity : AppCompatActivity() {

    private lateinit var adapter: NotesAdapter
    private val viewModel: NoteViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)


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
            else {
                Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show()
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

