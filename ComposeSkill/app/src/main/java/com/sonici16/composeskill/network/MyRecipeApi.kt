package com.sonici16.composeskill.network

import com.sonici16.composeskill.model.Root
import com.sonici16.composeskill.model.ShoppingResponse
import retrofit2.http.*

interface MyRecipeApi {
    @GET("{startIdx}/{endIdx}")
    suspend fun getRecipes(
        @Path("startIdx") startIdx: Int,
        @Path("endIdx") endIdx: Int,
        //@Path("category") category: String
    ): Root

    // 🔍 레시피 검색 API (메뉴명 검색)
    @GET("{startIdx}/{endIdx}/RCP_NM={category}")
    suspend fun searchTitleRecipes(
        @Path("startIdx") startIdx: Int,
        @Path("endIdx") endIdx: Int,
        @Path("category") category: String
    ): Root

    @GET("{startIdx}/{endIdx}/RCP_PARTS_DTLS={category}")
    suspend fun searchIngredRecipes(
        @Path("startIdx") startIdx: Int,
        @Path("endIdx") endIdx: Int,
        @Path("category") category: String
    ): Root

}