package com.mikkelthygesen.billsplit.ui.features.main

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.data.network.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Group
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel : BaseViewModel() {

    private val api = ServerApiImpl()

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

    fun addGroup() {
        val group = Group()
        updateUiState(AddGroup(group))
    }

    fun saveGroup(group: Group) {
        group.applyChanges()
        viewModelScope.launch {
            val req = runCatching { api.addGroup(group) }
            if(req.isSuccess) showGroup(group.id)
            else println("qqq Failed :( ${req.exceptionOrNull()}")
        }
    }

    fun getGroup(groupId: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val response = kotlin.runCatching { api.getGroup("p4Y79cmb9jTkOKhw4yMN") }
            if (response.isSuccess) println("qqq $response")
            else println("qqq ${response.exceptionOrNull()}")
        }
    }
}