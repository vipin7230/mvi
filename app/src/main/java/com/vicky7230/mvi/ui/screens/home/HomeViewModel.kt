package com.vicky7230.mvi.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vicky7230.mvi.data.Repository
import com.vicky7230.mvi.data.network.api.NetworkResult
import com.vicky7230.mvi.data.network.model.Todo
import com.vicky7230.mvi.stateMachine.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(), ContainerHost<HomeUiState, Nothing> {

    override val container =
        container<HomeUiState, Nothing>(HomeUiState())

    private val stateMachine = StateMachine.create<HomeState, HomeEvent, HomeSideEffect> {
        initialState(HomeState.Idle)

        state<HomeState.Idle> {
            on<HomeEvent.OnLoading> {
                transitionTo(HomeState.Loading, HomeSideEffect.Loading)
            }
        }

        state<HomeState.Loading> {
            on<HomeEvent.OnError> {
                transitionTo(HomeState.Error, HomeSideEffect.Error(it.error))
            }
            on<HomeEvent.OnSuccess> {
                transitionTo(HomeState.Success, HomeSideEffect.Success(it.list))
            }
        }

        state<HomeState.Success> {}
        state<HomeState.Error> {
            on<HomeEvent.OnLoading> {
                transitionTo(HomeState.Loading, HomeSideEffect.Loading)
            }
        }

        onTransition {
            val validTransition = it as? StateMachine.Transition.Valid ?: return@onTransition

            Timber.tag("FSM").e("\n=================================================")
            Timber.tag("FSM").e("From State: ${it.fromState}")
            Timber.tag("FSM").e("Event Fired = ${it.event::class.simpleName}")
            Timber.tag("FSM").e("State Transitioned to: ${it.toState}")

            when (val effect = validTransition.sideEffect as HomeSideEffect) {
                is HomeSideEffect.Loading -> intent { reduce { state.copy(loading = true) } }
                is HomeSideEffect.Error -> intent {
                    reduce {
                        state.copy(
                            loading = false,
                            error = effect.error
                        )
                    }
                }

                is HomeSideEffect.Success -> intent {
                    reduce {
                        state.copy(
                            error = "null",
                            loading = false,
                            todos = effect.list
                        )
                    }
                }
            }
        }
    }

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

    private fun updateState(networkResult: NetworkResult<List<Todo>>) {
        when (networkResult) {
            is NetworkResult.Loading -> {
                stateMachine.transition(HomeEvent.OnLoading)
            }

            is NetworkResult.Success -> {
                stateMachine.transition(HomeEvent.OnSuccess(networkResult.data))
            }

            is NetworkResult.Error -> {
                stateMachine.transition(HomeEvent.OnError(networkResult.message!!))
            }

            is NetworkResult.Exception -> {
                stateMachine.transition(HomeEvent.OnError(networkResult.throwable.localizedMessage!!))
            }
        }
    }
}