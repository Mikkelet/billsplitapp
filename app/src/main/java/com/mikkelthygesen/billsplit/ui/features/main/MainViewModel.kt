package com.mikkelthygesen.billsplit.ui.features.main

import android.net.Uri
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.data.auth.AuthProvider
import com.mikkelthygesen.billsplit.data.network.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : BaseViewModel() {

    private val api = ServerApiImpl()
    private val authProvider = AuthProvider()

    object Main : UiState
    object AddGroup : UiState
    object MyGroups : UiState
    object ShowProfile : UiState

    class ShowGroup(val groupId: String) : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Main)

    fun showMain() = updateUiState(UiState.Loading)

    fun showGroup(groupId: String) = emitUiEvent(ShowGroup(groupId))

    fun showMyGroups() = updateUiState(MyGroups)

    fun showProfile() = updateUiState(ShowProfile)

    fun showAddGroup() {
        updateUiState(AddGroup)
    }

    fun getNewGroup(user: Person): Group {
        return Group(
            id = "",
            name = "",
            people = listOf(user),
            createdBy = user,
            timeStamp = 0,
            events = listOf()
        )
    }

    suspend fun updateUser() {
        checkAuthStatusAsync {
            api.updateUser(it)
            authProvider.updateUserName(
                it.nameState,
                onSuccess = ::println,
                onFailure = ::println
            )
        }
    }

    fun uploadProfilePhoto(uri: Uri, onSuccess: () -> Unit) {
        checkAuthStatus {
            authProvider.updateProfilePicture(
                user = it,
                uri = uri,
                onSuccess = onSuccess,
                onFailure = this::handleError
            )
        }
    }

    suspend fun getGroups(): List<Group> {
        return checkAuthStatusAsync {
            api.getGroups(it.uid)
        }
    }

    suspend fun saveGroup(group: Group) {
        group.applyChanges()
        val groupResponse = api.addGroup(group)
        emitUiEvent(ShowGroup(groupResponse.id))
    }

    suspend fun getFriends(): List<Friend> {
        return checkAuthStatusAsync { user ->
            api.getFriends(user.uid)
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
        authProvider.signUpWithEmail(
            email, password,
            onSuccess = {
                showProfile()
            },
            onFailure = this::handleError
        )
    }

    fun signInEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        authProvider.signInWithEmail(
            email, password,
            onSuccess = { updateUiState(Main) },
            onFailure = this::handleError
        )
    }
}