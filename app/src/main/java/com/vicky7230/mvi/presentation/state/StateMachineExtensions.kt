package com.vicky7230.mvi.presentation.state

import com.vicky7230.mvi.presentation.stateMachine.StateMachine

inline fun <SIDE_EFFECT : Any> StateMachine.Transition<Any, Any, SIDE_EFFECT>.onValidSideEffect(
    invoke: (SIDE_EFFECT) -> Unit
) {
    val transition = this
    val validTransition = transition as? StateMachine.Transition.Valid
    validTransition?.sideEffect?.apply {
        invoke(this)
    }
}