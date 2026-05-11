package com.albin.demo.data

import retrofit2.http.GET

interface  PostsApiService {
    @GET ("api-comidas/comidas")
    suspend fun  getComidas(): List<Comida>
}