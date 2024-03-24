package com.vicky7230.mvi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.vicky7230.mvi.BuildConfig
import com.vicky7230.mvi.common.NetworkResult
import com.vicky7230.mvi.data.dto.TodoDto
import com.vicky7230.mvi.domain.model.Todo
import com.vicky7230.mvi.domain.usecase.GetTodosUseCase
import com.vicky7230.mvi.presentation.state.HomeEvent
import com.vicky7230.mvi.presentation.state.HomeSideEffect
import com.vicky7230.mvi.presentation.state.HomeStateMachine
import com.vicky7230.mvi.presentation.state.HomeUiState
import com.vicky7230.mvi.presentation.state.HomeUiSideEffect
import com.vicky7230.mvi.presentation.state.withLoggingIf
import com.vicky7230.mvi.presentation.stateMachine.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase
) : ViewModel(), ContainerHost<HomeUiState, HomeUiSideEffect> {

    override val container =
        container<HomeUiState, HomeUiSideEffect>(HomeUiState.Idle)

    private val stateMachine = HomeStateMachine(onTransitionEffect = { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.StartLoading -> intent {
                getTodosUseCase().collect { result: NetworkResult<List<Todo>> ->
                    updateState(result)
                }
                Timber.e("I am trying to see if this works....")
            }

            is HomeSideEffect.Loading -> {}
            is HomeSideEffect.Error -> {}
            is HomeSideEffect.Success -> {}
        }
    }).withLoggingIf { BuildConfig.DEBUG }


    private fun sendEventForTransition(event: HomeEvent) = intent {
        val transition = stateMachine.transition(event)
        if (transition is StateMachine.Transition.Valid) {
            reduce {
                transition.toState
            }
        } else {
            Timber.e("Invalid transition: $transition")
        }
    }

    init {
        getTodos()
    }

    fun getTodos() {
        sendEventForTransition(HomeEvent.StartLoading)
    }

    private fun updateState(networkResult: NetworkResult<List<Todo>>) {
        when (networkResult) {
            is NetworkResult.Loading -> {
                sendEventForTransition(HomeEvent.OnLoading)
            }

            is NetworkResult.Success -> {
                sendEventForTransition(HomeEvent.OnSuccess(networkResult.data))
            }

            is NetworkResult.Error -> {
                sendEventForTransition(HomeEvent.OnError(networkResult.message!!))
            }

            is NetworkResult.Exception -> {
                sendEventForTransition(HomeEvent.OnError(networkResult.throwable.localizedMessage!!))
            }
        }
    }
}