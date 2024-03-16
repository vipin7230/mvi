package com.vicky7230.mvi.data.network

import com.google.gson.JsonElement
import com.vicky7230.mvi.data.network.api.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ApiHelper {
    suspend fun getTodos(): Flow<NetworkResult<JsonElement>>
}