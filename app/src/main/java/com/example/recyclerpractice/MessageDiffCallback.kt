package com.example.recyclerpractice

import android.util.Log
import androidx.recyclerview.widget.DiffUtil

class MessageDiffCallback: DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(
        oldItem: Message,
        newItem: Message
    ): Boolean {
        return when {
            oldItem is Message.TextItem && newItem is Message.TextItem -> oldItem.id == newItem.id
            oldItem is Message.ImageItem && newItem is Message.ImageItem -> oldItem.id == newItem.id
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: Message,
        newItem: Message
    ): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Message, newItem: Message): Any? {
        Log.d("Hello12", "getChangePayload, ItemChanged $newItem")

        return if (oldItem is Message.TextItem && newItem is Message.TextItem && oldItem.message != newItem.message) {
            "TEXT_CHANGED"
        } else {
            null
        }
    }
}