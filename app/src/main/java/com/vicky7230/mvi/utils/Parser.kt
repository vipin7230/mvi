package com.vicky7230.mvi.utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.vicky7230.mvi.data.dto.TodoDto

fun parseTodoList(data: JsonElement): List<TodoDto> {
    val gson = Gson()
    return gson.fromJson(data, object : TypeToken<List<TodoDto>>() {}.type)
}