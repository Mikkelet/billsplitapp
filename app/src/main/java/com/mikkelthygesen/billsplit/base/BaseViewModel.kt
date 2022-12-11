package com.mikkelthygesen.billsplit.base

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.data.auth.NetworkExceptions
import com.mikkelthygesen.billsplit.data.auth.authProvider
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.CancellationException

abstract class BaseViewModel : ViewModel() {

    val loggedInUser: Person?
        get() = authProvider.loggedInUser

    val requireLoggedInUser: Person
        get() = loggedInUser!!

    interface DialogState {
        object DismissDialogs : DialogState
    }

    interface UiState {
        object Loading : UiState
        class Error(val exception: Exception) : UiState
        object SignIn : UiState
        object SignUp : UiState
    }

    interface UiEvent {
        object OnBackPressed : UiEvent
    }

    fun showSignIn() = updateUiState(UiState.SignIn)

    fun showSignUp() = updateUiState(UiState.SignUp)

    protected abstract val _mutableUiStateFlow: MutableStateFlow<UiState>
    val uiStateFlow: StateFlow<UiState> by lazy { _mutableUiStateFlow }

    protected val _mutableDialogStateFlow =
        MutableStateFlow<DialogState>(DialogState.DismissDialogs)
    val dialogState: StateFlow<DialogState> = _mutableDialogStateFlow

    private val _mutableUiEventsStateFlow = MutableSharedFlow<UiEvent>()
    val uiEventsState: SharedFlow<UiEvent> = _mutableUiEventsStateFlow

    fun onBackButtonPressed() {
        emitUiEvent(UiEvent.OnBackPressed)
    }

    fun showDialog(dialog: DialogState) {
        _mutableDialogStateFlow.value = dialog
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


    fun <T> checkAuthStatus(successCallback: (Person) -> T): T {
        if (authProvider.isUserLoggedIn())
            return successCallback(authProvider.loggedInUser!!)
        else throw Exception("User logged out")
    }

    @Composable
    fun CheckAuthStatusComposable(successCallback: @Composable (Person) -> Unit) {
        if (authProvider.isUserLoggedIn())
            successCallback(authProvider.loggedInUser!!)
    }

    suspend fun <T> checkAuthStatusAsync(successCallback: suspend (Person) -> T): T {
        if (authProvider.isUserLoggedIn())
            return successCallback(authProvider.loggedInUser!!)
        else throw NetworkExceptions.UserLoggedOut()
    }

    open fun handleError(exception: Throwable) {
        Timber.e(exception)
        when (exception) {
            is NetworkExceptions.UserLoggedOut -> showSignIn()
            else -> {
                Timber.e("qqq error=${exception.message}")
            }
        }
    }
}