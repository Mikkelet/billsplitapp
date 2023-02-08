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
    private val observeLocalGroupsUseCase: ObserveLocalGroupsUseCase
) : BaseViewModel() {

    object ShowGroups : UiState
    object ShowProfile : UiEvent
    data class ShowGroup(val group: Group) : UiEvent
    object AddGroup : UiEvent

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(ShowGroups)

    init {
        getGroups(true)
    }

    fun observeGroups(): Flow<List<Group>> = observeLocalGroupsUseCase.observe()

    fun getGroups(sync: Boolean) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val response = runCatching { getGroupsUseCase.execute(sync) }
            response.foldSuccess {
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