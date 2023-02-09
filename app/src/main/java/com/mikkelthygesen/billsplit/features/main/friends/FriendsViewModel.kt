package com.mikkelthygesen.billsplit.features.main.friends

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.models.Friend
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.domain.usecases.AcceptFriendRequestUseCase
import com.mikkelthygesen.billsplit.domain.usecases.AddFriendEmailUseCase
import com.mikkelthygesen.billsplit.domain.usecases.GetFriendsUseCase
import com.mikkelthygesen.billsplit.domain.usecases.ObserveLocalFriendsUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val addFriendEmailUseCase: AddFriendEmailUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val getFriendsUseCase: GetFriendsUseCase,
    private val observeLocalFriendsUseCase: ObserveLocalFriendsUseCase,
) : BaseViewModel() {

    object Friends : UiState

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Friends)
    var syncingFriends: Boolean by mutableStateOf(false)

    fun observeLocalFriends(): Flow<List<Friend>> = observeLocalFriendsUseCase()

    fun getFriends() {
        viewModelScope.launch {
            syncingFriends = true
            val response = runCatching {
                getFriendsUseCase.execute(true)
            }
            response.foldSuccess {
                syncingFriends = false
            }
        }
    }

    suspend fun acceptFriendRequest(friend: Person): Friend {
        return acceptFriendRequestUseCase.execute(friend)
    }

    suspend fun addFriend(email: String): Friend {
        return addFriendEmailUseCase.execute(email)
    }
}