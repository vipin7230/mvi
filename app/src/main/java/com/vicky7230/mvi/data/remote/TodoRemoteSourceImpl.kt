package com.vicky7230.mvi.data.remote

import com.vicky7230.mvi.data.dto.TodoDto
import javax.inject.Inject

class TodoRemoteSourceImpl @Inject
constructor(
    private val api: TodoApi,
) : TodoRemoteSource {
    override suspend fun getTodos(): List<TodoDto> {
        return api.getTodos()
    }
}