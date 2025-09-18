package com.sonici16.composeskill.ui.theme.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MyRecipeService {
    private const val BASE_URL = "https://openapi.foodsafetykorea.go.kr/api/74ea102bd31144cfb660/COOKRCP01/json/"

    val gson : Gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory()) // CoroutineCallAdapterFactory 추가
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()


    val api: MyRecipeApi = retrofit.create(MyRecipeApi::class.java)
}


