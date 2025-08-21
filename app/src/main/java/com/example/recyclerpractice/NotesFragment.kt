package com.example.recyclerpractice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerpractice.db.NoteDatabase
import kotlinx.coroutines.launch

class NotesFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        val db = NoteDatabase.getDatabase(requireContext())
        val repository = NotesRepository(db.noteDao())
        viewModel = ViewModelProvider(this, NotesViewModelFactory(repository))[NoteViewModel::class.java]

        adapter = NotesAdapter { note ->
            viewModel.incrementCount(note.id)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val editNote = view.findViewById<EditText>(R.id.editNote)
        val buttonAdd = view.findViewById<Button>(R.id.addButton)
        buttonAdd.setOnClickListener {
            val note = editNote.text.toString()
            if (note.isNotEmpty()) {
                viewModel.insert(note)
                editNote.text.clear()
            } else {
                Toast.makeText(requireContext(), "Note cannot be empty", Toast.LENGTH_SHORT).show()
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

        return view
    }
}