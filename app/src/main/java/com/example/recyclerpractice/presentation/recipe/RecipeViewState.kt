package com.example.recyclerpractice.presentation.recipe

import com.example.recyclerpractice.data.remote.model.Meal

sealed class RecipeViewState {
    object Loading: RecipeViewState()
    data class Success(val recipes: List<Meal>): RecipeViewState()
    data class Error(val message: String): RecipeViewState()
}