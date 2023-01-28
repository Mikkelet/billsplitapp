package com.mikkelthygesen.billsplit.features.main

import android.net.Uri
import com.mikkelthygesen.billsplit.domain.usecases.UploadProfilePictureUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,
) : BaseViewModel() {

    object AddGroup : UiState
    object MyGroups : UiState


    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(MyGroups)

    fun showMyGroups() = updateUiState(MyGroups)

    fun showAddGroup() = updateUiState(AddGroup)

    suspend fun uploadProfilePhoto(uri: Uri, onSuccess: () -> Unit) {
        val result = runCatching { uploadProfilePictureUseCase.execute(uri) }
        result.foldSuccess { onSuccess() }
    }

}