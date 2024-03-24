package com.vicky7230.mvi.presentation.state

import com.vicky7230.mvi.presentation.stateMachine.StateMachine

class HomeStateMachine(
    private val onTransitionEffect: (effect: HomeSideEffect) -> Unit,
    private val initialState: HomeUiState = HomeUiState.Idle
) : StateMachineContainer<HomeUiState, HomeEvent, HomeSideEffect> {

    override val stateMachineName = "HomeStateMachine"
    override val stateMachine: StateMachine<HomeUiState, HomeEvent, HomeSideEffect> by lazy {
        StateMachine.create<HomeUiState, HomeEvent, HomeSideEffect> {

            initialState(initialState)

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
                transition.onValidSideEffect {
                    onTransitionEffect(it)
                }
            }
        }
    }
}