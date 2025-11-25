package com.sonici16.composeskill.network

import com.sonici16.composeskill.model.ShoppingResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingRepository @Inject constructor(
    private val api: NaverShoppingApi
) {
    suspend fun search(query: String): ShoppingResponse {
        return api.searchShopping(query)
    }
}


//package com.sonici16.composeskill.network
//
//import android.util.Log
//import com.google.gson.JsonSyntaxException
//import com.sonici16.composeskill.model.Root
//import kotlinx.coroutines.delay
//import javax.inject.Inject
//import javax.inject.Singleton
//
////
//class RecipeRepository @Inject constructor(
//    private val apiService: MyRecipeApi
//) {
//
//    // 공통 재시도 함수
//    private suspend fun <T> retryApiCall(
//        maxRetry: Int = 3,
//        delayMillis: Long = 200,
//        block: suspend () -> T
//    ): T {
//        var lastError: Throwable? = null
//
//        repeat(maxRetry) { attempt ->
//            try {
//                return block()
//            } catch (e: JsonSyntaxException) {
//                // 지금 네가 겪는 그 에러
//                Log.e("RecipeRepository", "JsonSyntaxException on attempt $attempt: ${e.message}")
//                lastError = e
//            } catch (e: IllegalStateException) {
//                // "Expected BEGIN_OBJECT but was STRING" 이 보통 여기에 포함됨
//                Log.e("RecipeRepository", "IllegalStateException on attempt $attempt: ${e.message}")
//                lastError = e
//            } catch (e: Exception) {
//                // 그 외 네트워크 에러 등
//                Log.e("RecipeRepository", "Other error on attempt $attempt: ${e.message}")
//                lastError = e
//            }
//
//            // 다음 시도 전 잠깐 기다리기
//            delay(delayMillis)
//        }
//
//        // 여기까지 오면 maxRetry 번 전부 실패
//        throw lastError ?: RuntimeException("Unknown API error")
//    }
//
//    // 🔹 홈 상단(국) 등에서 사용하는 기본 레시피 호출
//    suspend fun fetchRecipes(startIdx: Int, endIdx: Int): Root {
//        return retryApiCall {
//            apiService.getRecipes(startIdx, endIdx)
//        }
//    }
//
//    // 🔹 검색 화면에서 사용하는 검색용 API
//    suspend fun searchTitleRecipes(startIdx: Int, endIdx: Int, category: String): Root {
//        return retryApiCall {
//            apiService.searchTitleRecipes(startIdx, endIdx, category)
//        }
//    }
//
//    suspend fun searchIngredRecipes(startIdx: Int, endIdx: Int, category: String): Root {
//        return retryApiCall {
//            apiService.searchIngredRecipes(startIdx, endIdx, category)
//        }
//    }
//}
//
