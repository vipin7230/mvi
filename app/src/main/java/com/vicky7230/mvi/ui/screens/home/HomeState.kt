package com.vicky7230.mvi.ui.screens.home

import com.vicky7230.mvi.data.network.model.Todo

sealed class HomeState {
    data object Loading : HomeState()
    data object Error : HomeState()
    data object Success : HomeState()
}

sealed class HomeEvent {
    data object OnLoading : HomeEvent()
    data object OnError : HomeEvent()
    data object OnSuccess : HomeEvent()
}

sealed class HomeSideEffect {
    data object Loading : HomeSideEffect()
    data object Error : HomeSideEffect()
    data object Success : HomeSideEffect()
}

data class HomeUiState(
    val loading: Boolean = false,
    val error: String = "null",
    val todos: List<Todo> = emptyList()
)