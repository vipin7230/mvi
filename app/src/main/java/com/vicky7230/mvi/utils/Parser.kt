package com.vicky7230.mvi.utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.vicky7230.mvi.data.network.model.Todo

fun parseTodoList(data: JsonElement): List<Todo> {
    val gson = Gson()
    return gson.fromJson(data, object : TypeToken<List<Todo>>() {}.type)
}