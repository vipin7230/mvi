package com.vicky7230.mvi.domain.usecase

import com.vicky7230.mvi.common.NetworkResult
import com.vicky7230.mvi.data.dto.toTodo
import com.vicky7230.mvi.domain.model.Todo
import com.vicky7230.mvi.domain.repository.TodoRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(
    private val todoRepository: TodoRemoteRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Todo>> {
        return try {
            val response = todoRepository.getTodos().map { it.toTodo() }
            NetworkResult.Success(response)
        } catch (httpException: HttpException) {
            Timber.e(httpException)
            NetworkResult.Error(
                code = httpException.code(),
                message = httpException.message(),
            )
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            NetworkResult.Exception(Exception(throwable.localizedMessage))
        }
    }
}