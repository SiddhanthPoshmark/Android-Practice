package com.example.recyclerpractice.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealResponse(
    @SerialName("meals")
    val meals: List<Meal>
)