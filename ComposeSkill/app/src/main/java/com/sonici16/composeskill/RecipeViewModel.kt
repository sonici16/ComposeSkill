package com.sonici16.composeskill

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonici16.composeskill.model.Row
import com.sonici16.composeskill.network.RecipeRepository
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

    // ------------ SearchScreen 상태 추가 ----------
    private val _searchResult = MutableStateFlow<List<Row>>(emptyList())
    val searchResult: StateFlow<List<Row>> = _searchResult


    // ------------ 공통 상태 --------------
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadMainRecipes(startIdx: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.fetchRecipes(startIdx, startIdx+9)
                _mainRecipes.value = response.COOKRCP01.row!!
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error"
                Log.e("RecipeViewModel", "loadMainRecipes error: $e")  // 로그 出力
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRecipesList(startIdx: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.fetchRecipes(startIdx, startIdx+9)
                _recipesList.value = response.COOKRCP01.row!!
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error"
                Log.e("RecipeViewModel", "loadMainRecipes error: $e")  // 로그 出力
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ------------ 검색 기능 추가 -------------
    fun searchByTitle(startIdx: Int = 1, endIdx: Int = 10, category: String = "반찬") {
        if (category.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.searchTitleRecipes(startIdx, endIdx, category)
                if(response.COOKRCP01.row!=null)
                    _searchResult.value = response.COOKRCP01.row
                else
                    _searchResult.value = emptyList()
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("RecipeViewModel", "loadMainRecipes error: $e")  // 로그 出力
                _searchResult.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun searchByIngredient(startIdx: Int = 1, endIdx: Int = 10, category: String = "반찬") {
        if (category.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.searchIngredRecipes(startIdx, endIdx, category)
                if(response.COOKRCP01.row!=null)
                    _searchResult.value = response.COOKRCP01.row
                else
                    _searchResult.value = emptyList()
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("RecipeViewModel", "loadMainRecipes error: $e")  // 로그 出力
                _searchResult.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSearchResult() {
        _searchResult.value = emptyList()
    }

}
