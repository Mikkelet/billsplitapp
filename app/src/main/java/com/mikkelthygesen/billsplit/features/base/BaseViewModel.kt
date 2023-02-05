package com.mikkelthygesen.billsplit.features.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import com.mikkelthygesen.billsplit.data.remote.exceptions.NetworkExceptions
import com.mikkelthygesen.billsplit.domain.models.Person
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var authProvider: AuthProvider

    val loggedIdUser: Person?
        get() = authProvider.userState

    val requireLoggedInUser: Person
        get() = authProvider.userState ?: throw NetworkExceptions.UserLoggedOutException

    interface DialogState {
        object DismissDialogs : DialogState
        class Error(val exception: Throwable) : DialogState
    }

    interface UiState {
        object Loading : UiState

    }

    interface UiEvent {
        object OnBackPressed : UiEvent
        object UserLoggedOut : UiEvent
    }

    protected abstract val _mutableUiStateFlow: MutableStateFlow<UiState>
    val uiStateFlow: StateFlow<UiState> by lazy { _mutableUiStateFlow }

    var dialogState by mutableStateOf<DialogState>(DialogState.DismissDialogs)

    private val _mutableUiEventsStateFlow = MutableSharedFlow<UiEvent>()
    val uiEventsState: SharedFlow<UiEvent> = _mutableUiEventsStateFlow

    fun onBackButtonPressed() {
        emitUiEvent(UiEvent.OnBackPressed)
    }

    protected fun showDialog(dialog: DialogState) {
        dialogState = dialog
    }

    fun dismissDialog() {
        dialogState = DialogState.DismissDialogs
    }

    fun emitUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _mutableUiEventsStateFlow.emit(event)
        }
    }

    fun updateUiState(event: UiState) {
        _mutableUiStateFlow.value = event
    }

    fun requireLoggedInUser(withUser: (Person) -> Unit) {
        if (loggedIdUser != null)
            withUser(requireLoggedInUser)
        else handleError(NetworkExceptions.UserLoggedOutException)
    }

    fun showLanding(){
        emitUiEvent(UiEvent.UserLoggedOut)
    }

    fun handleError(exception: Throwable) {
        Timber.e(exception)
        when (exception) {
            is java.util.concurrent.CancellationException -> Timber.e("java.util.concurrent.CancellationException")
            is NetworkExceptions.UserLoggedOutException -> emitUiEvent(UiEvent.UserLoggedOut)
            is io.ktor.client.network.sockets.SocketTimeoutException ->
                showDialog(DialogState.Error(Exception("Session timed out")))
            else -> {
                showDialog(DialogState.Error(exception))
            }
        }
    }

    protected fun <T> Result<T>.foldSuccess(onSuccess: (T) -> Unit) = fold(
        onSuccess = onSuccess,
        onFailure = ::handleError
    )
}