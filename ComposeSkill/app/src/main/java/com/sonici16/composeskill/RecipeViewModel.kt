package com.sonici16.composeskill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonici16.composeskill.ui.theme.model.Row
import com.sonici16.composeskill.ui.theme.network.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    // 메인 카드용 (국)
    private val _mainRecipes = MutableStateFlow<List<Row>>(emptyList())
    val mainRecipes: StateFlow<List<Row>> = _mainRecipes

    // 레시피 리스트용 (반찬)
    private val _recipesList = MutableStateFlow<List<Row>>(emptyList())
    val recipesList: StateFlow<List<Row>> = _recipesList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadMainRecipes(startIdx: Int = 1, endIdx: Int = 10, category: String = "국") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.fetchRecipes(startIdx, endIdx, category)
                _mainRecipes.value = response.COOKRCP01.row
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRecipesList(startIdx: Int = 1, endIdx: Int = 10, category: String = "반찬") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.fetchRecipes(startIdx, endIdx, category)
                _recipesList.value = response.COOKRCP01.row
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
