package com.example.colorlink.core.common.mvi

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

abstract class StateViewModel<State, Intent, Event>(initialState: State) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    abstract fun handleIntent(intent: Intent)

    protected fun updateState(reducer: State.() -> State) {
        _state.update(reducer)
    }

    protected fun emitEvent(event: Event) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

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

    protected open fun showError(message: String) {
        // To be implemented by subclasses if they want specific error handling
        // or just emit an event
    }
}
