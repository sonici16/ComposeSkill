package com.sonici16.composeskill.ui.theme.model

sealed class RecipeUiState {
    object Loading : RecipeUiState()
    data class Success(val recipes: List<Row>) : RecipeUiState()
    data class Error(val message: String) : RecipeUiState()
}
