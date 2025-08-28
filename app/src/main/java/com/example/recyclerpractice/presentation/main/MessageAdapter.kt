package com.example.recyclerpractice.presentation.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerpractice.R
import com.example.recyclerpractice.presentation.main.Message

class MessageAdapter(): ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    companion object {
        private const val TYPE_TEXT = 0
        private const val TYPE_IMAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is Message.TextItem -> TYPE_TEXT
            is Message.ImageItem -> TYPE_IMAGE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            TYPE_IMAGE -> {
                val view = inflater.inflate(R.layout.item_image, parent, false)
                ImageMessageViewHolder(view) { position -> onImageClicked(position)}
            }
            TYPE_TEXT -> {
                val view = inflater.inflate(R.layout.item_text, parent, false)
                TextMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        Log.d("Hello12", "onBindViewHolder, ItemChanged $position")
        when (val item = getItem(position)) {
            is Message.TextItem -> (holder as TextMessageViewHolder).bind(item)
            is Message.ImageItem -> (holder as ImageMessageViewHolder).bind(item)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            Log.d("Hello12", "onBindViewHolder with payloads, ItemChanged $position, payloads: $payloads")

            val payload = payloads[0]
            if (payload == "TEXT_CHANGED" && holder is TextMessageViewHolder) {
                val item = getItem(position) as Message.TextItem
                holder.updateText(item.message)
                return
            }
        }
        onBindViewHolder(holder, position)
    }

    private fun onImageClicked(position: Int) {
        val updatedList = currentList.toMutableList()
        val textIndex = position + 1
        if (textIndex < updatedList.size && updatedList[textIndex] is Message.TextItem) {
            val textItem = updatedList[textIndex] as Message.TextItem
            val originalText = "Message #${textIndex / 2 + 1}"
            val newText = if (textItem.message == originalText) "Hello World" else originalText
            updatedList[textIndex] = textItem.copy(message = newText)
            submitList(updatedList)
        }
    }
}

class TextMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.text_message)
    fun bind(item: Message.TextItem) {
        textView.text = item.message
    }
    fun updateText(newText: String) {
        textView.text = newText
    }
}

class ImageMessageViewHolder(itemView: View, private val clickCallback: (Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    fun bind(message: Message.ImageItem) {
        itemView.findViewById<ImageView>(R.id.text_image).setImageResource(message.imageResId)
        itemView.setOnClickListener { clickCallback(adapterPosition) }
    }
}