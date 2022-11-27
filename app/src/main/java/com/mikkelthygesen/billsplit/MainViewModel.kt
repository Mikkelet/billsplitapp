package com.mikkelthygesen.billsplit

import com.mikkelthygesen.billsplit.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : BaseViewModel() {

    object Main : UiState
    class ShowGroup(val groupId: String) : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Main)

    fun showMain() {
        _mutableUiStateFlow.value = Main
    }

    fun showGroup(id: String) {
        emitUiEvent(ShowGroup(id))
    }
}