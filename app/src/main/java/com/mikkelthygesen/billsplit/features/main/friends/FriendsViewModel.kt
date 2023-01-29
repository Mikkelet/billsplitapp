package com.mikkelthygesen.billsplit.features.main.friends

import com.mikkelthygesen.billsplit.domain.models.Friend
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.domain.usecases.AcceptFriendRequestUseCase
import com.mikkelthygesen.billsplit.domain.usecases.AddFriendEmailUseCase
import com.mikkelthygesen.billsplit.domain.usecases.GetFriendsUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val addFriendEmailUseCase: AddFriendEmailUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val getFriendsUseCase: GetFriendsUseCase,
) : BaseViewModel() {

    object Friends : UiState

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Friends)

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