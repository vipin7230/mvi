package com.vicky7230.mvi.domain.repository

import com.vicky7230.mvi.data.dto.TodoDto

interface TodoRemoteRepository {
    suspend fun getTodos(): List<TodoDto>
}