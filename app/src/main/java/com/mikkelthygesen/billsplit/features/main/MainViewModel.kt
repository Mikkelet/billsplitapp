package com.mikkelthygesen.billsplit.features.main

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.usecases.GetFriendsUseCase
import com.mikkelthygesen.billsplit.domain.usecases.GetGroupsUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getGroupsUseCase: GetGroupsUseCase,
    private val getFriendsUseCase: GetFriendsUseCase,
) : BaseViewModel() {

    object Main : UiState

    private var isUserSynchronized = false
    fun initialize() {
        if (!isUserSynchronized) syncUser()
    }

    fun uninitialize() {
        isUserSynchronized = false
    }

    private fun syncUser() {
        isUserSynchronized = true
        viewModelScope.launch {
            val response = runCatching {
                getGroupsUseCase()
                getFriendsUseCase()
            }
            response.foldError {
                isUserSynchronized = false
            }
        }
    }

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Main)
}