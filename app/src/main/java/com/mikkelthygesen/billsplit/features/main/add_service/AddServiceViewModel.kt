package com.mikkelthygesen.billsplit.features.main.add_service

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import com.mikkelthygesen.billsplit.domain.usecases.AddSubscriptionServiceUseCase
import com.mikkelthygesen.billsplit.domain.usecases.GetGroupUseCase
import com.mikkelthygesen.billsplit.domain.usecases.GetServiceFromLocalUseCase
import com.mikkelthygesen.billsplit.domain.usecases.UpdateSubscriptionServiceUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddServiceViewModel @Inject constructor(
    private val addSubscriptionServiceUseCase: AddSubscriptionServiceUseCase,
    private val getServiceFromLocalUseCase: GetServiceFromLocalUseCase,
    private val updateSubscriptionServiceUseCase: UpdateSubscriptionServiceUseCase,
    private val getGroupUseCase: GetGroupUseCase,
) : BaseViewModel() {

    object ServiceLoaded : UiState
    object ServiceAdded : UiEvent

    lateinit var service: SubscriptionService
    lateinit var group: Group

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)

    fun loadService(groupId: String, serviceId: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val response = runCatching {
                val service = getServiceFromLocalUseCase.execute(groupId, serviceId)
                val group = getGroupUseCase.execute(groupId)
                Pair(service, group)
            }
            response.foldSuccess {
                service = it.first
                group = it.second
                updateUiState(ServiceLoaded)
            }
        }
    }

    fun submitSubscriptionService() {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val response = runCatching {
                if (service.id.isBlank())
                    addSubscriptionServiceUseCase(group.id, service)
                else updateSubscriptionServiceUseCase(group.id, service)
            }
            response.fold(
                onSuccess = {
                    emitUiEvent(ServiceAdded)
                },
                onFailure = {
                    handleError(it)
                    updateUiState(ServiceLoaded)
                }
            )
        }
    }
}