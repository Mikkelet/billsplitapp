package com.mikkelthygesen.billsplit.ui.features.main

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.data.network.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : BaseViewModel() {

    private val api = ServerApiImpl()

    object Main : UiState
    object Settings : UiState
    object SignIn : UiState
    object SignUp : UiState
    class AddGroup(val group: Group) : UiState
    class Groups(val groups: List<Group>) : UiState

    class ShowGroup(val groupId: String) : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(SignUp)

    fun showMain() {
        updateUiState(Main)
    }

    fun showGroup(groupId: String) {
        emitUiEvent(ShowGroup(groupId))
    }

    fun showSignIn(){
        updateUiState(SignIn)
    }

    fun showSignUp(){
        updateUiState(SignUp)
    }

    fun addGroup() {
        val group = Group(
            id = "",
            createdBy = getLoggedIn(),
            people = listOf(getLoggedIn())
        )
        updateUiState(AddGroup(group))
    }

    fun getGroups() {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val response = kotlin.runCatching { api.getGroups(getLoggedIn().uid) }
            response.fold(
                onSuccess = {
                    updateUiState(Groups(it))
                },
                onFailure = Timber::e
            )
        }
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
        viewModelScope.launch { api.addFriend(userId) }
    }

    companion object {
        fun getLoggedIn() = Person("j6ETnEOrCpOJt7lBt2pU", "Mikkel")
    }
}