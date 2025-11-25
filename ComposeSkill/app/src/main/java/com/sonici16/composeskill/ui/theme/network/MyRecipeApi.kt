package com.sonici16.composeskill.ui.theme.network

import com.sonici16.composeskill.ui.theme.model.Root
import retrofit2.http.*

interface MyRecipeApi {
    @GET("{startIdx}/{endIdx}/RCP_PAT2={category}")
    suspend fun getRecipes(
        @Path("startIdx") startIdx: Int,
        @Path("endIdx") endIdx: Int,
        @Path("category") category: String //
    ): Root
}