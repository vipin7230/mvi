package com.vicky7230.mvi.data.network.api

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

interface Api {
    @GET("todos")
    suspend fun getTodos(
    ): Response<JsonElement>
}