package com.example.recyclerpractice.presentation.recipe

sealed class RecipeViewIntent {
    object LoadRandomRecipe: RecipeViewIntent()
    data class SearchRecipe(val query: String): RecipeViewIntent()
}