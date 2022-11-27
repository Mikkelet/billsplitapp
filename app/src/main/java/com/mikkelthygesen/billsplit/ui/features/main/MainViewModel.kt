package com.mikkelthygesen.billsplit.ui.features.main

import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.models.Group
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : BaseViewModel() {

    object Main : UiState
    object Settings : UiState
    class AddGroup(val group: Group) : UiState

    class ShowGroup(val groupId: String) : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Main)

    fun showMain() {
        updateUiState(Main)
    }

    fun showGroup(id: String) {
        emitUiEvent(ShowGroup(id))
    }

    fun addGroup(){
        val group = Group()
        updateUiState(AddGroup(group))
    }

    fun saveGroup(group: Group) {
        group.applyChanges()
    }
}