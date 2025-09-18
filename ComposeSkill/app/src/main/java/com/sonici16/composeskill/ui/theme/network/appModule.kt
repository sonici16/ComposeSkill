package com.sonici16.compose.myapplication.ui.theme.network

import com.sonici16.composeskill.ui.theme.network.MyRecipeApi
import com.sonici16.composeskill.ui.theme.network.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class) // ✅ 전역(Singleton) 범위로 모듈 등록
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://openapi.foodsafetykorea.go.kr/api/74ea102bd31144cfb660/COOKRCP01/json/") // ✅ 실제 API 주소 넣으세요
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipeApi(retrofit: Retrofit): MyRecipeApi {
        return retrofit.create(MyRecipeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: MyRecipeApi): RecipeRepository {
        return RecipeRepository(api)
    }
}
