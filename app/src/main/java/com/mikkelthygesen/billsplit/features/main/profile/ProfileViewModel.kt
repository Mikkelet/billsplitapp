package com.mikkelthygesen.billsplit.features.main.profile

import com.mikkelthygesen.billsplit.domain.usecases.UpdateNameUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateNameUseCase: UpdateNameUseCase,
) : BaseViewModel() {

    object Main : UiState

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Main)

    fun updateUserName(){

    }

}