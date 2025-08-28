package com.example.recyclerpractice.presentation.newFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recyclerpractice.databinding.FragmentNewBinding

object MessageManager {
    private val listeners = mutableListOf<() -> Unit>()

    fun registerListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: () -> Unit) {
        listeners.remove(listener)
    }
}
class NewFragment : Fragment() {
    private val listener: () -> Unit = {
        Log.d("Hello123", "Listener called! $this")
    }
    private var _binding: FragmentNewBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Hello123", "NewFragment OnCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Hello123", "NewFragment onViewCreated")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Hello123", "NewFragment onAttach")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Hello123", "NewFragment onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Hello123", "NewFragment onPause")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Hello123", "NewFragment onStart")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("Hello123", "NewFragment onDetach")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Hello123", "NewFragment onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Hello123", "NewFragment onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Hello123", "NewFragment onDestroyView")
        _binding = null
        MessageManager.unregisterListener(listener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Hello123", "NewFragment onCreateView")

        MessageManager.registerListener(listener)
        _binding = FragmentNewBinding.inflate(inflater, container, false)
        return binding.root
    }
}