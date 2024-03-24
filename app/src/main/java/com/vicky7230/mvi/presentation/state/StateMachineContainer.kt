package com.vicky7230.mvi.presentation.state

import com.vicky7230.mvi.presentation.stateMachine.StateMachine

interface StateMachineContainer<STATE: Any, EVENT: Any, SIDE_EFFECT:Any> {

    val stateMachineName: String
    val stateMachine: StateMachine<STATE, EVENT, SIDE_EFFECT>
    val state: STATE get() = stateMachine.state

    fun transition(event: EVENT): StateMachine.Transition<STATE, EVENT ,SIDE_EFFECT> =
        stateMachine.transition(event)
}