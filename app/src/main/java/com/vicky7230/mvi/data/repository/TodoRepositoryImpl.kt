package com.vicky7230.mvi.data.repository

import com.vicky7230.mvi.data.dto.TodoDto
import com.vicky7230.mvi.data.remote.TodoApi
import com.vicky7230.mvi.domain.repository.TodoRepository
import javax.inject.Inject

class TodoRepositoryImpl @Inject
constructor(
    private val api: TodoApi,
) : TodoRepository {
    override suspend fun getTodos(): List<TodoDto> {
        return api.getTodos()
    }
}