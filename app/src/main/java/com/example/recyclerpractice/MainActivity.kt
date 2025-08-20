package com.example.recyclerpractice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private lateinit var adapter: MessageAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.message_list)
        var messages = mutableListOf<Message>()

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
            Log.d("Hello12", "mainactivity, List Updated")
            messages.add(Message.ImageItem(id = i * 2 - 1, imageResId = android.R.drawable.btn_star_big_on))
            messages.add(Message.TextItem(id = i * 2, message = "Message #$i"))
        }
        adapter.submitList(messages)

        findViewById<Button>(R.id.btnGoNext).setOnClickListener {
            val intent = Intent(this, NotesActivity::class.java)
            startActivity(intent)
        }
    }
}