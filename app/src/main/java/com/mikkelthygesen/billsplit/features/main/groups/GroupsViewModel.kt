package com.mikkelthygesen.billsplit.features.main.groups

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.usecases.GetGroupsUseCase
import com.mikkelthygesen.billsplit.domain.usecases.ObserveLocalGroupsUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val getGroupsUseCase: GetGroupsUseCase,
    private val observeLocalGroupsUseCase: ObserveLocalGroupsUseCase,
) : BaseViewModel() {

    object ShowGroups : UiState
    object OnProfileClicked : UiEvent
    data class OnGroupClicked(val group: Group) : UiEvent
    object AddGroupClicked : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(ShowGroups)

    fun observeGroups(): Flow<List<Group>> = observeLocalGroupsUseCase.observe()

    fun syncGroups() {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val response = runCatching { getGroupsUseCase() }
            response.fold(
                onSuccess = {
                    updateUiState(ShowGroups)
                },
                onFailure = {
                    updateUiState(ShowGroups)
                }
            )
        }
    }

    fun onProfilePictureClicked() {
        emitUiEvent(OnProfileClicked)
    }

    fun onGroupClicked(group: Group) {
        emitUiEvent(OnGroupClicked(group))
    }

    fun onAddGroupClicked() {
        emitUiEvent(AddGroupClicked)
    }
}