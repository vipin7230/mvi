package com.vicky7230.mvi.domain.usecase

import com.vicky7230.mvi.common.NetworkResult
import com.vicky7230.mvi.data.dto.toTodo
import com.vicky7230.mvi.domain.model.Todo
import com.vicky7230.mvi.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(): Flow<NetworkResult<List<Todo>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = todoRepository.getTodos().map { it.toTodo() }
            emit(NetworkResult.Success(response))
        } catch (httpException: HttpException) {
            Timber.e(httpException)
            emit(
                NetworkResult.Error(
                    code = httpException.code(),
                    message = httpException.message(),
                ),
            )
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            emit(NetworkResult.Exception(Exception(throwable.localizedMessage)))
        }
    }.flowOn(Dispatchers.IO)
}