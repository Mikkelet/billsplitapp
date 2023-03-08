package com.mikkelthygesen.billsplit.features.main.group

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val addEventUseCase: AddEventUseCase,
    private val getGroupUseCase: GetGroupUseCase,
    private val observeLocalEventsUseCase: ObserveLocalEventsUseCase,
    private val observeLocalServicesUseCase: ObserveLocalServicesUseCase,
    private val observeDebtForLoggedInUserUseCase: ObserveDebtForLoggedInUserUseCase
) : BaseViewModel() {
    object Chat : UiState
    object ShowDebt : UiState
    object Services : UiState
    object OnAddExpenseClicked : UiEvent
    data class OnEditExpenseClicked(val expenseId: String) : UiEvent
    data class OnServiceClicked(val service: SubscriptionService) : UiEvent

    var showChatLoader by mutableStateOf(false)
        private set

    lateinit var group: Group
        private set

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)

    fun debtFlow(): Flow<List<Pair<Person, Float>>> =
        observeDebtForLoggedInUserUseCase.observe(group.id)

    fun eventsFlow(): Flow<List<Event>> = observeLocalEventsUseCase
        .observe(group.id)
        .map { events -> events.sortedBy { it.timeStamp }.reversed() }

    fun getGroup(groupId: String) {
        if (this::group.isInitialized) return
        viewModelScope.launch {
            // call get cached group
            val cacheResponse = runCatching {
                getGroupUseCase.execute(groupId, false)
            }
            cacheResponse.foldSuccess { group ->
                this@GroupViewModel.group = group
                updateUiState(Chat)
            }

            // then sync with remote
            val syncResponse = runCatching {
                showChatLoader = true
                getGroupUseCase.execute(groupId, true)
            }
            syncResponse.foldSuccess { group ->
                this@GroupViewModel.group.updateGroup(group)
            }
            showChatLoader = false
        }
    }

    fun servicesFlow(): Flow<List<SubscriptionService>> =
        observeLocalServicesUseCase.observe(group.id)
            .map { services -> services.sortedBy { it.nameState } }

    fun addExpense() {
        emitUiEvent(OnAddExpenseClicked)
    }

    suspend fun addPayment(paidTo: Person, amount: Float) {
        val payment = Payment(
            createdBy = requireLoggedInUser,
            paidTo = paidTo,
            amount = amount
        )
        addEventUseCase.execute(group, payment)
    }

    fun showDebt() {
        updateUiState(ShowDebt)
    }

    fun showServices() {
        updateUiState(Services)
    }

    fun editSharedExpense(sharedExpense: GroupExpense) {
        emitUiEvent(OnEditExpenseClicked(sharedExpense.id))
    }

    fun showChat() {
        updateUiState(Chat)
    }

    fun handleBack(): Boolean {
        return when (uiStateFlow.value) {
            is ShowDebt,
            is Services -> {
                showChat()
                true
            }
            else -> false
        }
    }

    fun onServiceClicked(subscriptionService: SubscriptionService) {
        emitUiEvent(OnServiceClicked(subscriptionService))
    }
}