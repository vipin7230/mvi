package com.vicky7230.mvi.domain.repository

import com.vicky7230.mvi.domain.model.Todo

interface TodoRemoteRepository {
    suspend fun getTodos(): List<Todo>
}