package com.mikkelthygesen.billsplit.features.main

import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : BaseViewModel() {

    object AddGroup : UiState
    object MyGroups : UiState

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(MyGroups)

    fun showMyGroups() = updateUiState(MyGroups)

    fun showAddGroup() = updateUiState(AddGroup)
}