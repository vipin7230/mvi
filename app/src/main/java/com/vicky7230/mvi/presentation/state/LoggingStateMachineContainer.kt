package com.vicky7230.mvi.presentation.state

import android.util.Log
import com.vicky7230.mvi.presentation.stateMachine.StateMachine

class LoggingStateMachineContainer<STATE : Any, EVENT : Any, SIDE_EFFECT : Any>(
    private val actual: StateMachineContainer<STATE, EVENT, SIDE_EFFECT>
) : StateMachineContainer<STATE, EVENT, SIDE_EFFECT> {

    override val stateMachineName: String
        get() = actual.stateMachineName

    override val stateMachine: StateMachine<STATE, EVENT, SIDE_EFFECT> = actual.stateMachine.with {
        onTransition { transition ->
            if (transition is StateMachine.Transition.Valid) {
                Log.d(
                    stateMachineName,
                    "{{==from==}} ${transition.fromState} {{==on==}} ${transition.event} {{==to==}} ${transition.toState}"
                )
            } else {
                Log.w(
                    stateMachineName,
                    "{{==from==}} ${transition.fromState} {{==on==}} ${transition.event} {{==to==}} <invalid>"
                )
            }
        }
    }
}

inline fun <STATE : Any, EVENT : Any, SIDE_EFFECT : Any> StateMachineContainer<STATE, EVENT, SIDE_EFFECT>.withLoggingIf(
    needLogging: () -> Boolean
): StateMachineContainer<STATE, EVENT, SIDE_EFFECT> {
    return if (needLogging()) {
        LoggingStateMachineContainer(this)
    } else {
        this
    }
}