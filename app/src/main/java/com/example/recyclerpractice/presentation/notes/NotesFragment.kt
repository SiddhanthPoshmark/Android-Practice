package com.example.recyclerpractice.presentation.notes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerpractice.R
import com.example.recyclerpractice.presentation.newFragment.NewFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private lateinit var adapter: NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Hello123", "NotesFragment OnCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Hello123", "NotesFragment onViewCreated")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Hello123", "NotesFragment onAttach")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Hello123", "NotesFragment onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Hello123", "NotesFragment onPause")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Hello123", "NotesFragment onStart")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("Hello123", "NotesFragment onDetach")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Hello123", "NotesFragment onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Hello123", "NotesFragment onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Hello123", "NotesFragment onDestroyView")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Hello123","NotesFragment onCreateView")
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        val viewModel: NoteViewModel by viewModels()

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
        viewLifecycleOwner.lifecycleScope.launch {
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

        val openDetailButton = view.findViewById<Button>(R.id.openNewFragment)
        openDetailButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, NewFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}