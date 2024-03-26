package com.vicky7230.mvi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.vicky7230.mvi.BuildConfig
import com.vicky7230.mvi.common.NetworkResult
import com.vicky7230.mvi.domain.model.Todo
import com.vicky7230.mvi.domain.usecase.GetTodosUseCase
import com.vicky7230.mvi.presentation.state.HomeEvent
import com.vicky7230.mvi.presentation.state.HomeSideEffect
import com.vicky7230.mvi.presentation.state.HomeStateMachine
import com.vicky7230.mvi.presentation.state.HomeUiState
import com.vicky7230.mvi.presentation.state.HomeUiSideEffect
import com.vicky7230.mvi.presentation.stateMachine.withLoggingIf
import com.vicky7230.mvi.presentation.stateMachine.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
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
            is HomeSideEffect.Loading -> intent {
                val response = getTodosUseCase()
                updateState(response)
                Timber.e("I am trying to see if this works....")
            }
            is HomeSideEffect.Error -> intent{
                postSideEffect(HomeUiSideEffect.SuccessToast(sideEffect.error))
            }
            is HomeSideEffect.Success -> intent{
                postSideEffect(HomeUiSideEffect.SuccessToast("Data retrieved successfully"))
            }
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
        sendEventForTransition(HomeEvent.OnLoading)
    }

    private fun updateState(networkResult: NetworkResult<List<Todo>>) {
        when (networkResult) {
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