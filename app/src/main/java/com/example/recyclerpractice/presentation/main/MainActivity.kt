package com.example.recyclerpractice.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recyclerpractice.R
import com.example.recyclerpractice.presentation.recipe.RecipeFinder
import com.example.recyclerpractice.databinding.ActivityMainBinding
import com.example.recyclerpractice.presentation.notes.NotesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.messageList
        val messages = mutableListOf<Message>()

        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        recyclerView.layoutManager = gridLayoutManager
        adapter = MessageAdapter()
        recyclerView.adapter = adapter

        for(i in 1 .. 50) {
            messages.add(Message.ImageItem(id = i * 2 - 1, imageResId = android.R.drawable.btn_star_big_on))
            messages.add(Message.TextItem(id = i * 2, message = "Message #$i"))
        }
        adapter.submitList(messages)

        binding.btnGoNext.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container, NotesFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnGoComposeActivity.setOnClickListener {
            val intent = Intent(this, RecipeFinder::class.java)
            startActivity(intent)
        }
    }
}