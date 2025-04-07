package com.stickermaker.app.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TenorService {
    @GET("search")
    suspend fun searchGifs(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("limit") limit: Int = 20
    ): Response<TenorResponse>
}

data class TenorResponse(
    val results: List<TenorResult>
)

data class TenorResult(
    val id: String,
    val title: String,
    val media_formats: Map<String, MediaFormat>
)

data class MediaFormat(
    val url: String,
    val dims: List<Int>,
    val duration: Float,
    val size: Int
)

object TenorApi {
    private const val BASE_URL = "https://tenor.googleapis.com/v2/"
    private const val API_KEY = "LIVDSRZULELA" // Ganti dengan API key yang valid

    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val service: TenorService = retrofit.create(TenorService::class.java)

    suspend fun searchGifs(query: String): List<TenorResult> {
        return try {
            val response = service.searchGifs(query, API_KEY)
            if (response.isSuccessful) {
                response.body()?.results ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}