package com.mikkelthygesen.billsplit.ui.features.main

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.data.auth.AuthProvider
import com.mikkelthygesen.billsplit.data.network.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : BaseViewModel() {

    private val api = ServerApiImpl()
    private val authProvider = AuthProvider()

    object Main : UiState
    object Settings : UiState
    object SignIn : UiState
    object SignUp : UiState
    class AddGroup(val group: Group) : UiState
    class Groups(val groups: List<Group>) : UiState

    class ShowGroup(val groupId: String) : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Main)

    fun showMain() {
        updateUiState(UiState.Loading)
    }

    fun showGroup(groupId: String) {
        emitUiEvent(ShowGroup(groupId))
    }

    fun showSignIn() {
        updateUiState(SignIn)
    }

    fun showSignUp() {
        updateUiState(SignUp)
    }

    fun addGroup() {
        checkAuthStatus(
            successCallback = {
                val group = Group(
                    id = "",
                    createdBy = it,
                    people = listOf(it)
                )
                updateUiState(AddGroup(group))
            })
    }

    fun getGroups() {
        checkAuthStatus(
            successCallback = {
                updateUiState(UiState.Loading)
                viewModelScope.launch {
                    val response = kotlin.runCatching { api.getGroups(it.uid) }
                    response.fold(
                        onSuccess = {
                            updateUiState(Groups(it))
                        },
                        onFailure = Timber::e
                    )
                }
            }
        )
    }

    fun saveGroup(group: Group) {
        updateUiState(UiState.Loading)
        group.applyChanges()
        viewModelScope.launch {
            val req = runCatching { api.addGroup(group) }
            req.fold(
                onSuccess = {
                    if (uiStateFlow.value == UiState.Loading)
                        emitUiEvent(ShowGroup(it.id))
                },
                onFailure = Timber::e
            )
        }
    }

    fun addFriend(userId: String) {
        updateUiState(UiState.Loading)
        checkAuthStatus {
            viewModelScope.launch { api.addFriend(it.uid, userId) }
        }
    }

    fun signUpEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        authProvider.signUpWithEmail(email, password,
            onSuccess = {
                updateUiState(Main)
            },
            onFailure = {
                updateUiState(SignUp)
            })
    }

    override fun onLoggedOutCallback() {
        updateUiState(SignIn)
    }

    fun signInEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        authProvider.signInWithEmail(email, password,
            onSuccess = {
                updateUiState(Main)
            },
            onFailure = {
                updateUiState(SignUp)
            })
    }
}