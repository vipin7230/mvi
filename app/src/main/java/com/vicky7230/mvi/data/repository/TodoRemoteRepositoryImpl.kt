package com.vicky7230.mvi.data.repository

import com.vicky7230.mvi.data.dto.TodoDto
import com.vicky7230.mvi.data.remote.TodoRemoteSource
import com.vicky7230.mvi.domain.repository.TodoRemoteRepository
import javax.inject.Inject

class TodoRemoteRepositoryImpl @Inject constructor(
    private val todoRemoteResource: TodoRemoteSource
): TodoRemoteRepository {
    override suspend fun getTodos(): List<TodoDto> {
        return todoRemoteResource.getTodos()
    }
}