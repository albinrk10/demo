package com.albin.demo.data


import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient{
    private  val json = Json{
        ignoreUnknownKeys = true
    }

    private const val  BASE_URL ="https://6a0171ea36fb6ad04de0ef68.mockapi.io/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val comidasService: PostsApiService = retrofit.create(PostsApiService::class.java)

}
