package com.example.recyclerpractice.presentation.main

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerpractice.presentation.notes.NotesFragment
import com.example.recyclerpractice.R
import dagger.hilt.android.AndroidEntryPoint

private lateinit var adapter: MessageAdapter

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.message_list)
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

        findViewById<Button>(R.id.btnGoNext).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container, NotesFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}