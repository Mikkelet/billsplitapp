package com.mikkelthygesen.billsplit.features.main.add_group

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.usecases.AddGroupUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
    private val addGroupUseCase: AddGroupUseCase
) : BaseViewModel() {

    object AddName : UiState
    object AddParticipants : UiState
    object Ready : UiState

    data class GroupAdded(val group: Group) : UiEvent

    fun getNewGroup() =
        Group(
            id = "",
            name = "",
            people = listOf(requireLoggedInUser),
            createdBy = requireLoggedInUser,
            timeStamp = 0,
            events = listOf(),
            debts = emptyList()
        )

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(AddName)

    var submittingGroup by mutableStateOf(false)

    fun saveGroup(group: Group) {
        submittingGroup = true
        viewModelScope.launch {
            val response = runCatching { addGroupUseCase.execute(group) }
            response.foldSuccess {
                emitUiEvent(GroupAdded(it))
            }
        }
    }

    fun next() {
        when (uiStateFlow.value) {
            is AddName -> updateUiState(AddParticipants)
            is AddParticipants -> updateUiState(Ready)
        }
    }

    fun back() {
        if (submittingGroup) return
        when (uiStateFlow.value) {
            is AddParticipants -> updateUiState(AddName)
            is Ready -> updateUiState(AddParticipants)
        }
    }

    fun reset() {
        updateUiState(AddName)
        submittingGroup = false
    }

}