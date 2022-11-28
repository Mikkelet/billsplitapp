package com.mikkelthygesen.billsplit.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    interface DialogState {
        object DismissDialogs : DialogState
    }

    interface UiState {
        object Loading : UiState
        class Error(val exception: Exception) : UiState
    }

    interface UiEvent {
        object OnBackPressed : UiEvent
    }

    abstract val _mutableUiStateFlow: MutableStateFlow<UiState>
    val uiStateFlow: StateFlow<UiState> by lazy { _mutableUiStateFlow }

    protected val _mutableDialogStateFlow =
        MutableStateFlow<DialogState>(DialogState.DismissDialogs)
    val dialogState: StateFlow<DialogState> = _mutableDialogStateFlow

    private val _mutableUiEventsStateFlow = MutableSharedFlow<UiEvent>()
    val uiEventsState: SharedFlow<UiEvent> = _mutableUiEventsStateFlow

    fun onBackButtonPressed() {
        emitUiEvent(UiEvent.OnBackPressed)
    }

    fun dismissDialog() {
        _mutableDialogStateFlow.value = DialogState.DismissDialogs
    }

    fun emitUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _mutableUiEventsStateFlow.emit(event)
        }
    }

    fun updateUiState(event: UiState) {
        _mutableUiStateFlow.value = event
    }
}