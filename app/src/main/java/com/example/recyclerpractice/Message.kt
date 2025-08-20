package com.example.recyclerpractice

sealed class Message {
    data class TextItem(val id: Int, val message: String) : Message()
    data class ImageItem(val id: Int, val imageResId: Int) : Message()
}
