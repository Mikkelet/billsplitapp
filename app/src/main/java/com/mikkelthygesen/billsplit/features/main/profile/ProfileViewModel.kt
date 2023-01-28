package com.mikkelthygesen.billsplit.features.main.profile

import com.mikkelthygesen.billsplit.domain.usecases.UpdateNameUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateNameUseCase: UpdateNameUseCase
) : BaseViewModel() {

    object Profile : UiState
    object FriendsPressed : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Profile)

    fun showFriends() {
        emitUiEvent(FriendsPressed)
    }

    fun signOut() {
        authProvider.signOut()
    }
}