package com.mikkelthygesen.billsplit.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.data.auth.NetworkExceptions
import com.mikkelthygesen.billsplit.data.auth.authProvider
import com.mikkelthygesen.billsplit.models.Person
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    val loggedInUser: Person?
        get() = authProvider.loggedInUser

    val requireLoggedInUser: Person
        get() = loggedInUser!!

    interface DialogState {
        object DismissDialogs : DialogState
        class Error(val exception: Throwable) : DialogState
    }

    interface UiState {
        object Loading : UiState
        object SignIn : UiState
        object SignUp : UiState
    }

    interface UiEvent {
        object OnBackPressed : UiEvent
    }

    fun showSignIn() {
        updateUiState(UiState.SignIn)
    }

    fun showSignUp() {
        updateUiState(UiState.SignUp)
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

    fun <T> checkAuthStatus(successCallback: (Person) -> T) {
        if (authProvider.loggedInUser != null)
            successCallback(requireLoggedInUser)
        else handleError(NetworkExceptions.UserLoggedOutException)
    }

    suspend fun <T> checkAuthStatusAsync(successCallback: suspend (Person) -> T): T {
        if (authProvider.isUserLoggedIn())
            return successCallback(authProvider.loggedInUser!!)
        else throw NetworkExceptions.UserLoggedOutException
    }

    fun handleError(exception: Throwable) {
        Timber.e(exception)
        when (exception) {
            is java.util.concurrent.CancellationException -> Timber.e("java.util.concurrent.CancellationException")
            is NetworkExceptions.UserLoggedOutException -> showSignIn()
            else -> {
                showDialog(DialogState.Error(exception))
            }
        }
    }

    protected fun <T> Result<T>.foldOrHandleError(onSuccess:(T)->Unit) = fold(
        onSuccess = onSuccess,
        onFailure = ::handleError
    )

    protected fun <T> Result<T>.foldEmptySucces() = fold(
        onSuccess = {},
        onFailure = ::handleError
    )
}