package com.mikkelthygesen.billsplit.ui.features.main

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.ui.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: ServerApiImpl,
) : BaseViewModel() {

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

    suspend fun updateUser() = authProvider.updateUserName()

    suspend fun uploadProfilePhoto(uri: Uri, onSuccess: () -> Unit) {
        val result = runCatching { authProvider.updateProfilePicture(uri) }
        result.foldSuccess { onSuccess() }
    }

    suspend fun getGroups(sync: Boolean = true): List<Group> {
        return api.getGroups(sync)
    }

    suspend fun saveGroup(group: Group) {
        group.applyChanges()
        val groupResponse = api.addGroup(group)
        emitUiEvent(ShowGroup(groupResponse.id))
    }

    suspend fun getFriends(sync: Boolean = false): List<Friend> {
        return api.getFriends(sync)
    }

    suspend fun acceptFriendRequest(friend: Person): Friend {
        return api.addFriendUserId(requireLoggedInUser.uid, friend)
    }

    suspend fun addFriend(email: String): Friend {
        return api.addFriendEmail(requireLoggedInUser.uid, email)
    }

    fun signUpEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val result = kotlin.runCatching { authProvider.signUpWithEmail(email, password) }
            result.foldSuccess { showProfile() }
        }
    }

    fun signInEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val result = runCatching { authProvider.signInWithEmail(email, password) }
            result.foldSuccess { showProfile() }
        }
    }
}