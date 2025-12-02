package com.sonici16.composeskill

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonici16.composeskill.model.CategoryNode
import com.sonici16.composeskill.model.ShoppingItem
import com.sonici16.composeskill.navigaton.Screen
import com.sonici16.composeskill.network.NaverShoppingApi
import com.sonici16.composeskill.util.buildCategoryTree
import com.sonici16.composeskill.util.loadCategoryCsv
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NaverShoppingViewModel @Inject constructor(
    private val api: NaverShoppingApi
) : ViewModel() {

    // 상단 배너용 데이터
    private val _mainItems = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val mainItems: StateFlow<List<ShoppingItem>> = _mainItems

    // 홈 화면 리스트용 데이터
    private val _itemsList = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val itemsList: StateFlow<List<ShoppingItem>> = _itemsList

    // 검색 결과 리스트
    private val _searchResults = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val searchResults: StateFlow<List<ShoppingItem>> = _searchResults

    // 로딩 여부 (중복 요청 방지 및 로딩 표시)
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // 에러 메시지
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // 페이징 상태값
    private var currentQuery: String = ""
    private var currentStart = 1               // 현재 요청 시작 위치
    private val displayCount = 30              // 한 페이지 당 요청 개수


    private val _tree = MutableStateFlow<List<CategoryNode>>(emptyList())
    val categoryTree: StateFlow<List<CategoryNode>> = _tree


    // 현재 선택된 카테고리 경로 (e.g. [대,중,소])
    // 선택 경로 (Breadcrumb 용)
    private val _selectedPath = MutableStateFlow<List<CategoryNode>>(emptyList())
    val selectedPath: StateFlow<List<CategoryNode>> = _selectedPath
    /**
     * 카테고리 상품 결과 + 로딩 상태 관리
     */
    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items: StateFlow<List<ShoppingItem>> = _items



    /**
     * 홈 화면 초기 로딩
     */
    fun load(query: String = "고양이집") {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = api.searchShopping(query, display = 30)

                // 첫 5개는 배너, 나머지는 리스트로 분리
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

    /**
     * 검색 실행
     * 첫 페이지부터 다시 요청함
     */
    fun search(query: String) {
        if (query.isBlank()) return

        currentQuery = query
        currentStart = 1  // 첫 페이지부터

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = api.searchShopping(
                    display = displayCount,
                    query = query,
                    start = currentStart
                )
                _searchResults.value = response.items ?: emptyList()

            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * 검색 결과 페이징 (스크롤 끝에서 호출됨)
     */
    fun loadNextPage() {
        // 이미 로딩 중이거나 검색어가 없으면 중단
        if (_loading.value) return
        if (currentQuery.isBlank()) return

        // 다음 페이지로 이동
        currentStart += displayCount

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = api.searchShopping(
                    query = currentQuery,
                    display = displayCount,
                    start = currentStart
                )

                val newItems = response.items ?: emptyList()
                // 기존 데이터 + 새로 로딩한 데이터
                _searchResults.value = _searchResults.value + newItems

            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * 검색 초기화 (검색창 들어가면 이전 결과 제거)
     */
    fun resetSearch() {
        _searchResults.value = emptyList()
    }

    // 카테고리 선택
    fun selectCategory(node: CategoryNode) {
        _selectedPath.value = _selectedPath.value + node

        if (node.children.isEmpty()) {
            loadCategoryProducts(node.name) //  딱 여기 한 번만 호출
        } else {
            // 중간 Depth → 상품 숨기기
            _items.value = emptyList()
        }
    }



    fun initCategory(context: Context) {
        viewModelScope.launch {
            try {
                val rows = loadCategoryCsv(context)
                val tree = buildCategoryTree(rows)
                _tree.value = tree
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    /**
     * Breadcrumb 뒤로가기 (상위 카테고리로 이동)
     * @return true → 화면에서 pop 안 하고 카테고리 UI만 갱신
     * @return false → 더 이상 뒤로갈 Depth 없음 → Activity/Navigation에 popBackStack 요청
     */
    fun popCategory(): Boolean {
        val path = _selectedPath.value

        if (path.isEmpty()) return false

        val newPath = path.dropLast(1)
        _selectedPath.value = newPath

        if (newPath.isEmpty()) {
            // 최상위로 돌아갈 경우 → 상품 숨기기
            _items.value = emptyList()
        }

        //  절대 재검색하지 않음!!!
        return true
    }


    fun loadCategoryProducts(categoryName: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = api.searchShopping(
                    query = categoryName,
                    display = 30
                )
                _items.value = response.items ?: emptyList() // CategoryScreen에서 확인하는 데이터
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }


    fun clearItems() {
        _items.value = emptyList()
        popCategory()
    }
}
