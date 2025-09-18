package com.sonici16.composeskill.ui.theme.network
import com.sonici16.composeskill.ui.theme.model.Root
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val apiService: MyRecipeApi
) {
    // API에서 레시피 데이터를 가져오는 함수
    suspend fun fetchRecipes(startIdx: Int, endIdx: Int, category: String): Root {
        return apiService.getRecipes(startIdx, endIdx, category)
    }
}