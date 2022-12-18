package com.mikkelthygesen.billsplit.ui.features.main

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.data.auth.AuthProvider
import com.mikkelthygesen.billsplit.data.network.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

    private val api = ServerApiImpl()
    private val authProvider = AuthProvider()

    object Main : UiState
    object AddGroup : UiState
    object MyGroups : UiState
    object ShowProfile : UiState

    class ShowGroup(val groupId: String) : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(MyGroups)

    fun showGroup(groupId: String) = emitUiEvent(ShowGroup(groupId))

    fun showMyGroups() = updateUiState(MyGroups)

    fun showProfile() = updateUiState(ShowProfile)

    fun showAddGroup() = updateUiState(AddGroup)

    fun getNewGroup(user: Person): Group {
        return Group(
            id = "",
            name = "",
            people = listOf(user),
            createdBy = user,
            timeStamp = 0,
            events = listOf(),
            debts = emptyList()
        )
    }

    suspend fun updateUser() {
        checkAuthStatusAsync { user ->
            val result = runCatching { authProvider.updateUserName(user.nameState) }
            result.fold(
                onSuccess = { user.saveChanges() },
                onFailure = this::handleError
            )
        }
    }

    suspend fun uploadProfilePhoto(uri: Uri, onSuccess: () -> Unit) {
        checkAuthStatusAsync {
            val result = runCatching { authProvider.updateProfilePicture(it, uri) }
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = ::handleError
            )
        }
    }

    suspend fun getGroups(sync: Boolean = true): List<Group> {
        return checkAuthStatusAsync {
            api.getGroups(it.uid, sync)
        }
    }

    suspend fun saveGroup(group: Group) {
        group.applyChanges()
        val groupResponse = api.addGroup(group)
        emitUiEvent(ShowGroup(groupResponse.id))
    }

    suspend fun getFriends(sync: Boolean = false): List<Friend> {
        return checkAuthStatusAsync { user ->
            api.getFriends(user.uid, sync)
        }
    }

    suspend fun acceptFriendRequest(friend: Person): Friend {
        return checkAuthStatusAsync { user ->
            api.addFriendUserId(user.uid, friend)
        }
    }

    suspend fun addFriend(email: String): Friend {
        return checkAuthStatusAsync {
            api.addFriendEmail(it.uid, email)
        }
    }

    fun signUpEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val result = kotlin.runCatching { authProvider.signUpWithEmail(email, password) }
            result.fold(
                onSuccess = { showProfile() },
                onFailure = {
                    handleError(it)
                    updateUiState(UiState.SignIn)
                }
            )
        }
    }

    fun signInEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val result = runCatching { authProvider.signInWithEmail(email, password) }
            result.fold(
                onSuccess = { showProfile() },
                onFailure = {
                    showProfile()
                    updateUiState(AddGroup)
                },
            )
        }
    }
}