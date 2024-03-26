package com.vicky7230.mvi.data.remote

import com.vicky7230.mvi.data.dto.TodoDto

interface TodoRemoteSource {
    suspend fun getTodos(): List<TodoDto>
}