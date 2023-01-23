package com.mikkelthygesen.billsplit.features.main

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.usecases.*
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.domain.models.Friend
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addGroupUseCase: AddGroupUseCase,
    private val addFriendEmailUseCase: AddFriendEmailUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val getFriendsUseCase: GetFriendsUseCase,
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,
    private val updateNameUseCase: UpdateNameUseCase,
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

    suspend fun updateUser() = updateNameUseCase.execute()

    suspend fun uploadProfilePhoto(uri: Uri, onSuccess: () -> Unit) {
        val result = runCatching { uploadProfilePictureUseCase.execute(uri) }
        result.foldSuccess { onSuccess() }
    }

    suspend fun saveGroup(group: Group) {
        val groupResponse = addGroupUseCase.execute(group)
        emitUiEvent(ShowGroup(groupResponse.id))
    }

    suspend fun getFriends(sync: Boolean = false): List<Friend> {
        return getFriendsUseCase.execute(sync)
    }

    suspend fun acceptFriendRequest(friend: Person): Friend {
        return acceptFriendRequestUseCase.execute(friend)
    }

    suspend fun addFriend(email: String): Friend {
        return addFriendEmailUseCase.execute(email)
    }

    fun signUpEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val result = runCatching { signUpWithEmailUseCase.execute(email, password) }
            result.foldSuccess { showProfile() }
        }
    }

    fun signInEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val result = runCatching { signInWithEmailUseCase.execute(email, password) }
            result.foldSuccess { showProfile() }
        }
    }
}