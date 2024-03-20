package com.vicky7230.mvi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.vicky7230.mvi.common.NetworkResult
import com.vicky7230.mvi.data.dto.TodoDto
import com.vicky7230.mvi.domain.model.Todo
import com.vicky7230.mvi.domain.usecase.GetTodosUseCase
import com.vicky7230.mvi.presentation.state.HomeEvent
import com.vicky7230.mvi.presentation.state.HomeSideEffect
import com.vicky7230.mvi.presentation.state.HomeUiState
import com.vicky7230.mvi.presentation.state.HomeUiSideEffect
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

    private val stateMachine = StateMachine.create<HomeUiState, HomeEvent, HomeSideEffect> {
        initialState(HomeUiState.Idle)

        state<HomeUiState.Idle> {
            on<HomeEvent.StartLoading> {
                dontTransition(
                    sideEffect = HomeSideEffect.StartLoading
                )
            }

            on<HomeEvent.OnLoading> {
                transitionTo(
                    state = HomeUiState.Loading,
                    sideEffect = HomeSideEffect.Loading
                )
            }
        }

        state<HomeUiState.Loading> {
            on<HomeEvent.OnError> {
                transitionTo(
                    state = HomeUiState.Error(it.error),
                    sideEffect = HomeSideEffect.Error(it.error)
                )
            }
            on<HomeEvent.OnSuccess> {
                transitionTo(
                    state = HomeUiState.DataRetrieved(it.list),
                    sideEffect = HomeSideEffect.Success(it.list)
                )
            }
        }

        state<HomeUiState.DataRetrieved> {

        }

        state<HomeUiState.Error> {
            on<HomeEvent.StartLoading> {
                dontTransition(
                    sideEffect = HomeSideEffect.StartLoading
                )
            }
        }

        onTransition { transition: StateMachine.Transition<HomeUiState, HomeEvent, HomeSideEffect> ->
            val validTransition =
                transition as? StateMachine.Transition.Valid ?: return@onTransition

            Timber.tag("FSM").e("\n=================================================")
            Timber.tag("FSM").e("From State: ${transition.fromState}")
            Timber.tag("FSM").e("Event Fired = ${transition.event::class.simpleName}")
            Timber.tag("FSM").e("State Transitioned to: ${transition.toState}")

            when (val sideEffect = validTransition.sideEffect as HomeSideEffect) {
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
        }
    }

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
        sendEventForTransition(HomeEvent.StartLoading)
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