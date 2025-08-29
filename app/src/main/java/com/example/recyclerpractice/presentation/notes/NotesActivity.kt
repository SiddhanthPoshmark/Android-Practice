package com.example.recyclerpractice.presentation.notes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerpractice.R
import com.example.recyclerpractice.databinding.ActivityNotesBinding
import kotlinx.coroutines.launch

class NotesActivity : AppCompatActivity() {

    private lateinit var adapter: NotesAdapter
    private val viewModel: NoteViewModel by viewModels()

    private lateinit var binding: ActivityNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NotesAdapter { note ->
            viewModel.incrementCount(note.id)
        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val editNote = binding.editNote
        val buttonAdd = binding.addButton
        buttonAdd.setOnClickListener {
            val note = editNote.text.toString()
            if (note.isNotBlank()) {
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

