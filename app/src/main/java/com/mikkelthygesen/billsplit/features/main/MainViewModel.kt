package com.mikkelthygesen.billsplit.features.main

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.usecases.*
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.domain.models.Friend
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addFriendEmailUseCase: AddFriendEmailUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val getFriendsUseCase: GetFriendsUseCase,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,
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

    suspend fun uploadProfilePhoto(uri: Uri, onSuccess: () -> Unit) {
        val result = runCatching { uploadProfilePictureUseCase.execute(uri) }
        result.foldSuccess { onSuccess() }
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
}