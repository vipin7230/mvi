package com.vicky7230.mvi.data

import com.google.gson.JsonElement
import com.vicky7230.mvi.data.network.ApiHelper
import com.vicky7230.mvi.data.network.api.NetworkResult
import com.vicky7230.mvi.data.network.model.Todo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImp @Inject
constructor(
    private val apiHelper: ApiHelper
) : Repository {
    override suspend fun getTodos(): Flow<NetworkResult<List<Todo>>> {
        return apiHelper.getTodos()
    }
}