package com.vicky7230.mvi.ui.screens.home

import com.vicky7230.mvi.data.network.model.Todo

sealed class HomeState {
    data object Idle : HomeState()
    data object Loading : HomeState()
    data object Error : HomeState()
    data object Success : HomeState()
}

sealed class HomeEvent {
    data object OnLoading : HomeEvent()
    data class OnError(val error: String) : HomeEvent()
    data class OnSuccess(val list: List<Todo>) : HomeEvent()
}

sealed class HomeSideEffect {
    data object Loading : HomeSideEffect()
    data class Error(val error: String) : HomeSideEffect()
    data class Success(val list: List<Todo>) : HomeSideEffect()
}

data class HomeUiState(
    val loading: Boolean = false,
    val error: String = "null",
    val todos: List<Todo> = emptyList()
)