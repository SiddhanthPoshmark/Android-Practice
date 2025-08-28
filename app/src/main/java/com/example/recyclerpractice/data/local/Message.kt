package com.example.recyclerpractice.data.local

sealed class Message {
    data class TextItem(val id: Int, val message: String) : Message()
    data class ImageItem(val id: Int, val imageResId: Int) : Message()
}