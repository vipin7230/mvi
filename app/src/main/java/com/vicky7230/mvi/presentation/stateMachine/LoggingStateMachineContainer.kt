package com.vicky7230.mvi.presentation.stateMachine

import timber.log.Timber

class LoggingStateMachineContainer<STATE : Any, EVENT : Any, SIDE_EFFECT : Any>(
    private val actual: StateMachineContainer<STATE, EVENT, SIDE_EFFECT>
) : StateMachineContainer<STATE, EVENT, SIDE_EFFECT> {

    override val stateMachineName: String
        get() = actual.stateMachineName

    override val stateMachine: StateMachine<STATE, EVENT, SIDE_EFFECT> = actual.stateMachine.with {
        onTransition { transition ->
            if (transition is StateMachine.Transition.Valid) {
                /*Log.d(
                    stateMachineName,
                    "{{==from==}} ${transition.fromState} {{==on==}} ${transition.event} {{==to==}} ${transition.toState}"
                )*/

                Timber.tag("FSM").e("\n=================================================")
                Timber.tag("FSM").e("From State: ${transition.fromState}")
                Timber.tag("FSM").e("Event Fired = ${transition.event}")
                Timber.tag("FSM").e("State Transitioned to: ${transition.toState}")

            } else {
                /*Log.w(
                    stateMachineName,
                    "{{==from==}} ${transition.fromState} {{==on==}} ${transition.event} {{==to==}} <invalid>"
                )*/

                Timber.tag("FSM").e("\n=================================================")
                Timber.tag("FSM").e("From State: ${transition.fromState}")
                Timber.tag("FSM").e("Event Fired = ${transition.event}")
                Timber.tag("FSM").e("State Transitioned to: <invalid>")
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