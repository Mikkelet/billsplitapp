package com.mikkelthygesen.billsplit.features.main.groups

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.usecases.GetGroupsUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val getGroupsUseCase: GetGroupsUseCase,
) : BaseViewModel() {

    object ShowGroups : UiState
    object ShowProfile : UiEvent
    data class ShowGroup(val group: Group) : UiEvent
    object AddGroup : UiEvent

    var groups by mutableStateOf<List<Group>>(emptyList())

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)

    fun getGroups(sync: Boolean) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val response = runCatching { getGroupsUseCase.execute(sync) }
            response.foldSuccess {
                groups = it
                updateUiState(ShowGroups)
            }
        }
    }

    fun onProfilePictureClicked() {
        emitUiEvent(ShowProfile)
    }

    fun showGroup(group: Group) {
        emitUiEvent(ShowGroup(group))
    }

    fun addGroup() {
        emitUiEvent(AddGroup)
    }
}