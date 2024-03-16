package com.vicky7230.mvi.data.network.api

sealed class NetworkResult<out T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()

    class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()

    class Exception<T : Any>(val throwable: Throwable) : NetworkResult<T>()

    data object Loading : NetworkResult<Nothing>()
}