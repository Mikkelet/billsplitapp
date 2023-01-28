package com.mikkelthygesen.billsplit.features.main.profile

import android.net.Uri
import com.mikkelthygesen.billsplit.domain.usecases.UpdateNameUseCase
import com.mikkelthygesen.billsplit.domain.usecases.UploadProfilePictureUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateNameUseCase: UpdateNameUseCase,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,

    ) : BaseViewModel() {

    object Profile : UiState
    object FriendsPressed : UiEvent
    object ProfilePictureUploaded : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Profile)

    fun showFriends() {
        emitUiEvent(FriendsPressed)
    }

    suspend fun uploadProfilePhoto(uri: Uri) {
        val result = runCatching { uploadProfilePictureUseCase.execute(uri) }
        result.foldSuccess { emitUiEvent(ProfilePictureUploaded) }
    }

    fun signOut() {
        authProvider.signOut()
    }
}