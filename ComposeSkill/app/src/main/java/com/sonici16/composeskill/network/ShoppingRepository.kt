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

