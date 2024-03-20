package com.vicky7230.mvi.domain.repository

import com.vicky7230.mvi.common.NetworkResult
import com.vicky7230.mvi.data.dto.TodoDto
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun getTodos(): List<TodoDto>
}