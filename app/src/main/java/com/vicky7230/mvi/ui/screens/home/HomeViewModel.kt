package com.vicky7230.mvi.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.vicky7230.mvi.data.Repository
import com.vicky7230.mvi.data.network.api.NetworkResult
import com.vicky7230.mvi.utils.parseTodoList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getTodos()
    }

    fun getTodos() {
        viewModelScope.launch {
            repository.getTodos().collect {
                updateState(it)
            }
        }
    }

    private suspend fun updateState(networkResult: NetworkResult<JsonElement>) {
        when (networkResult) {
            is NetworkResult.Loading -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        loading = true
                    )
                }
            }

            is NetworkResult.Success -> {
                val listJson = networkResult.data
                withContext(Dispatchers.IO) {
                    val deserializedList = parseTodoList(listJson)
                    _uiState.update { currentState ->
                        currentState.copy(
                            error = "null",
                            loading = false,
                            todos = deserializedList,
                        )
                    }
                }
            }

            is NetworkResult.Error -> {
                var errorMessage = networkResult.message
                _uiState.update { currentState ->
                    currentState.copy(
                        loading = false,
                        error = errorMessage!!
                    )
                }
            }

            is NetworkResult.Exception -> {
                var exceptionMessage = networkResult.throwable.localizedMessage
                _uiState.update { currentState ->
                    currentState.copy(
                        loading = false,
                        error = exceptionMessage!!
                    )
                }
            }
        }
    }
}