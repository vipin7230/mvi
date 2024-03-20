package com.vicky7230.mvi.data.remote

import com.vicky7230.mvi.data.dto.TodoDto
import retrofit2.Response
import retrofit2.http.GET
interface TodoApi {
    @GET("todos")
    suspend fun getTodos(
    ): List<TodoDto>
}