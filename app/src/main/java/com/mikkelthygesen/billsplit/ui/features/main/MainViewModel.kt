package com.mikkelthygesen.billsplit.ui.features.main

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.data.auth.AuthProvider
import com.mikkelthygesen.billsplit.data.network.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.sampleGroup
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.ui.features.main.profile.ProfileView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : BaseViewModel() {

    private val api = ServerApiImpl()
    private val authProvider = AuthProvider()

    object Main : UiState
    object SignIn : UiState
    object SignUp : UiState
    class AddGroup(val group: Group) : UiState
    object MyGroups : UiState
    object ShowProfile : UiState

    class AddFriendToGroupDialog(val friends: List<Friend>) : DialogState

    class ShowGroup(val groupId: String) : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Main)

    fun showMain() = updateUiState(UiState.Loading)

    fun showGroup(groupId: String) = emitUiEvent(ShowGroup(groupId))

    fun showSignIn() = updateUiState(SignIn)

    fun showSignUp() = updateUiState(SignUp)

    fun showMyGroups() = updateUiState(MyGroups)

    fun showProfile() = updateUiState(ShowProfile)

    fun addGroup() {
        checkAuthStatus {
            val group = Group(
                id = "",
                createdBy = it,
                people = listOf(it)
            )
            updateUiState(AddGroup(group))
        }
    }

    suspend fun getGroups(): List<Group> {
        return checkAuthStatusAsync {
            api.getGroups(it.uid)
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

    suspend fun getFriends() : List<Friend> {
        return checkAuthStatusAsync { user ->
            api.getFriends(user.uid)
        }
    }

    fun ShowAddFriendToGroupDialog(friends: List<Friend>) {
        showDialog(AddFriendToGroupDialog(friends))
    }

    suspend fun acceptFriendRequest(userId: String, friend: Person): Friend {
        return api.addFriendUserId(userId, friend)
    }

    suspend fun addFriend(userId: String, email: String): Friend {
        return api.addFriendEmail(userId, email)
    }

    fun signUpEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        authProvider.signUpWithEmail(email, password,
            onSuccess = {
                showProfile()
            },
            onFailure = { updateUiState(SignUp) })
    }

    fun signInEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        authProvider.signInWithEmail(email, password,
            onSuccess = { updateUiState(Main) },
            onFailure = { updateUiState(SignUp) })
    }
}