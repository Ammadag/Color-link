package com.example.colorlink.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Base ViewModel for MVI architecture.
 *
 * @param State Immutable UI state
 * @param Intent User actions
 * @param Event One-time side effects (navigation, alerts, haptics)
 */
abstract class StateViewModel<State, Intent, Event>(initialState: State) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    /**
     * Main entry point for user actions.
     */
    abstract fun handleIntent(intent: Intent)

    /**
     * Updates the current state using the provided reducer.
     */
    protected fun updateState(reducer: State.() -> State) {
        _state.update(reducer)
    }

    /**
     * Emits a one-time event.
     */
    protected fun emitEvent(event: Event) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    /**
     * Launches a coroutine safely within the viewModelScope.
     */
    protected fun launchSafely(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(context) {
            try {
                block()
            } catch (e: Exception) {
                showError(e.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Handles error reporting. Can be overridden for custom logic.
     */
    protected open fun showError(message: String) {
        // Implementation can emit a generic error event or log the error
    }
}
