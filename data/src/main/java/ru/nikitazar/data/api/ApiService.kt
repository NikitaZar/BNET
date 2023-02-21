package ru.nikitazar.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.nikitazar.data.BuildConfig
import ru.nikitazar.data.models.Drug

fun loggingInterceptor() = HttpLoggingInterceptor()
    .apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

fun okhttp(vararg interceptors: Interceptor): OkHttpClient = OkHttpClient.Builder()
    .apply {
        interceptors.forEach {
            this.addInterceptor(it)
        }
    }
    .build()

fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BuildConfig.BASE_URL)
    .client(client)
    .build()


interface ApiService {

    @GET("index")
    suspend fun getPage(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("search") search: String,
    ): Response<List<Drug>>

    @GET("item")
    suspend fun getById(
        @Query("id") id: Int,
    ): Response<Drug>
}