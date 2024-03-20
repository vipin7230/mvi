package com.vicky7230.mvi.data.network.api

import com.google.gson.JsonElement
import com.vicky7230.mvi.data.network.model.Todo
import retrofit2.Response
import retrofit2.http.GET

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

interface Api {
    @GET("todos")
    suspend fun getTodos(
    ): Response<List<Todo>>
}