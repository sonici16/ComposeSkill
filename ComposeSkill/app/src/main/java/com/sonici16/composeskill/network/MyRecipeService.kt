package com.sonici16.composeskill.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MyRecipeService {
//    private const val BASE_URL = "https://openapi.foodsafetykorea.go.kr/api/74ea102bd31144cfb660/COOKRCP01/json/"
//    private const val BASE_URL2 = "https://openapi.naver.com/v1/search/shop.json/"
//
//
//
//    val gson : Gson = GsonBuilder()
//        .setLenient()
//        .create()
//
//
//    val client = OkHttpClient.Builder()
//        .addInterceptor { chain ->
//            val request = chain.request()
//                .newBuilder()
//                .addHeader("X-Naver-Client-Id", "application/json")  // 예시 헤더
//                .addHeader("X-Naver-Client-Secret", "Bearer your_token_here") // 필요한 경우
//                .build()
//
//            chain.proceed(request)
//        }
//        .build()
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .addCallAdapterFactory(CoroutineCallAdapterFactory()) // CoroutineCallAdapterFactory 추가
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .build()
//
//
//    val api: MyRecipeApi = retrofit.create(MyRecipeApi::class.java)
}


