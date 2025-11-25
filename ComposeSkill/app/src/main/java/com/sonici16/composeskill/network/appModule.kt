package com.sonici16.composeskill.network
import com.sonici16.composeskill.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class) // ✅ 전역(Singleton) 범위로 모듈 등록
object AppModule {
//    @Provides
//    @Singleton
//    fun provideRetrofit(): Retrofit {
//
//        val gson : Gson = GsonBuilder()
//            .setLenient()
//            .create()
//
//        return Retrofit.Builder()
//            .baseUrl("http://openapi.foodsafetykorea.go.kr/api/74ea102bd31144cfb660/COOKRCP01/json/") // ✅ 실제 API 주소 넣으세요
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideRecipeApi(retrofit: Retrofit): MyRecipeApi {
//        return retrofit.create(MyRecipeApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideRepository(api: MyRecipeApi): RecipeRepository {
//        return RecipeRepository(api)
//    }
//
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .addHeader("X-Naver-Client-Id", BuildConfig.API_Client)
                    .addHeader("X-Naver-Client-Secret", BuildConfig.API_Secret)
                    .build()

                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): NaverShoppingApi =
        retrofit.create(NaverShoppingApi::class.java)
}
