package com.vicky7230.mvi.data.dto

import com.vicky7230.mvi.domain.model.Todo

data class TodoDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)

fun TodoDto.toTodo(): Todo {
    return Todo(
        id = id,
        title = title,
        completed = completed
    )
}