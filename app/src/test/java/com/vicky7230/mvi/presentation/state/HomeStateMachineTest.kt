package com.vicky7230.mvi.presentation.state

import com.vicky7230.mvi.domain.model.Todo
import com.vicky7230.mvi.presentation.stateMachine.StateMachine
import kotlin.test.assertTrue
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class HomeStateMachineTest {

    @Parameterized.Parameter(value = 0)
    lateinit var fromState: HomeUiState

    @Parameterized.Parameter(value = 1)
    lateinit var event: HomeEvent

    @Parameterized.Parameter(value = 2)
    lateinit var toState: HomeUiState

    @Parameterized.Parameter(value = 3)
    lateinit var sideEffect: HomeSideEffect

    @Test
    fun `given fromState with transitionEvent should goto expectedState`() {
        // given
        val stateMachine = HomeStateMachine({}, fromState)

        // when
        val transition = stateMachine.transition(event)

        // Then
        assertTrue {
            transition is StateMachine.Transition.Valid
        }
        require(transition is StateMachine.Transition.Valid)
        assertEquals(toState, transition.toState)
        assertEquals(sideEffect, transition.sideEffect)
    }

    companion object {

        private val fakeData = listOf(
            Todo(4, "Task1", true),
            Todo(4, "Task1", true),
        )

        private const val fakeError = "Fake error"

        @Parameterized.Parameters(
            name = "{index} - fromState: {0}; event: {1}; toState: {2}; sideEffect: {3}"
        )
        @JvmStatic
        fun data() = listOf(
            // on Loading
            arrayOf(
                HomeUiState.Idle,
                HomeEvent.OnLoading,
                HomeUiState.Loading,
                HomeSideEffect.Loading
            ),
            // On Error
            arrayOf(
                HomeUiState.Loading,
                HomeEvent.OnError(fakeError),
                HomeUiState.Error(fakeError),
                HomeSideEffect.Error(fakeError)
            ),
            //on Success
            arrayOf(
                HomeUiState.Loading,
                HomeEvent.OnSuccess(fakeData),
                HomeUiState.DataRetrieved(fakeData),
                HomeSideEffect.Success(fakeData)
            ),
            // on retry when error
            arrayOf(
                HomeUiState.Error(fakeError),
                HomeEvent.OnLoading,
                HomeUiState.Loading,
                HomeSideEffect.Loading
            )
        )
    }


}