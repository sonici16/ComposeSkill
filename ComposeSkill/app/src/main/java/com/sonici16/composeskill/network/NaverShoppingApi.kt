package com.sonici16.composeskill.network

import com.sonici16.composeskill.model.ShoppingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverShoppingApi {

    @GET("v1/search/shop.json")
    suspend fun searchShopping(
        @Query("query") query: String,
        @Query("display") display: Int = 10,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "sim"
    ): ShoppingResponse
}
