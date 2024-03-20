package com.vicky7230.mvi.presentation.state

import com.vicky7230.mvi.domain.model.Todo


sealed class HomeEvent {
    data object StartLoading : HomeEvent()
    data object OnLoading : HomeEvent()
    data class OnError(val error: String) : HomeEvent()
    data class OnSuccess(val list: List<Todo>) : HomeEvent()
}

sealed class HomeSideEffect {
    data object StartLoading: HomeSideEffect()
    data object Loading : HomeSideEffect()
    data class Error(val error: String) : HomeSideEffect()
    data class Success(val list: List<Todo>) : HomeSideEffect()
}

sealed class HomeUiState {
    data object Idle: HomeUiState()
    data object Loading: HomeUiState()
    data class Error(val error: String): HomeUiState()
    data class DataRetrieved(val todos: List<Todo>): HomeUiState()
}

sealed class HomeUiSideEffect {
    data class SuccessToast(val msg: String) : HomeUiSideEffect()
    data class ErrorToast(val msg: String) : HomeUiSideEffect()
}