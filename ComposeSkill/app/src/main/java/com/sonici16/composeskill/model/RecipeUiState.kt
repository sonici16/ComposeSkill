package com.sonici16.composeskill.model

sealed class RecipeUiState {
    object Loading : RecipeUiState()
    data class Success(val recipes: List<Row>) : RecipeUiState()
    data class Error(val message: String) : RecipeUiState()
}
