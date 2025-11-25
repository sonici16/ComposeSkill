package com.sonici16.composeskill

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonici16.composeskill.model.ShoppingItem
import com.sonici16.composeskill.network.NaverShoppingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NaverShoppingViewModel @Inject constructor(
    private val api: NaverShoppingApi
) : ViewModel() {

    // 🔥 메인(상단 배너용)
    private val _mainItems = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val mainItems: StateFlow<List<ShoppingItem>> = _mainItems

    // 🔥 리스트(하단 가로/세로 리스트)
    private val _itemsList = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val itemsList: StateFlow<List<ShoppingItem>> = _itemsList

    // 🔥 검색 결과(SearchScreen)
    private val _searchResults = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val searchResults: StateFlow<List<ShoppingItem>> = _searchResults


    // 🔥 공통 상태
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    // -------------------------------------------------------------------
    //  홈에서 첫 로딩 (기본 검색어: 고양이집)
    // -------------------------------------------------------------------
    fun load(query: String = "고양이집") {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = api.searchShopping(query, display = 30)

                _mainItems.value = response.items.take(5)
                _itemsList.value = response.items.drop(5)

            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("NaverShoppingViewModel", "load error: $e")
            } finally {
                _loading.value = false
            }
        }
    }

    // -------------------------------------------------------------------
    //  검색 기능(SearchScreen)
    // -------------------------------------------------------------------
    fun search(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = api.searchShopping(query, display = 30)
                _searchResults.value = response.items

            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("NaverShoppingViewModel", "search error: $e")
                _searchResults.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetSearch() {
        _searchResults.value = emptyList()
    }
}
