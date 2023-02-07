package com.mikkelthygesen.billsplit.features.main.group

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.DebtCalculator
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import com.mikkelthygesen.billsplit.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val addEventUseCase: AddEventUseCase,
    private val getGroupUseCase: GetGroupUseCase,
    private val getServicesFromLocalUseCase: GetServicesFromLocalUseCase,
    private val getEventsFromLocalUseCase: GetEventsFromLocalUseCase
) : BaseViewModel() {

    object Chat : UiState
    object ShowDebt : UiState
    object Services : UiState
    object OnAddExpenseClicked : UiEvent
    data class OnEditExpenseClicked(val expenseId: String) : UiEvent
    data class OnServiceClicked(val service: SubscriptionService) : UiEvent

    var showChatLoader by mutableStateOf(false)
        private set

    private val _people = mutableListOf<Person>()
    val people: List<Person> = _people
    lateinit var group: Group
        private set

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    private val _mutableEventsStateFlow = MutableStateFlow<List<Event>>(emptyList())
    val eventStateFlow: StateFlow<List<Event>> = _mutableEventsStateFlow

    suspend fun getLocalEvents(): List<Event> {
        return getEventsFromLocalUseCase.execute(group.id)
    }

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

    suspend fun getLocalServices(): List<SubscriptionService> =
        getServicesFromLocalUseCase.execute(group.id)

    fun addExpense() {
        emitUiEvent(OnAddExpenseClicked)
    }

    suspend fun addPayment(paidTo: Person, amount: Float) {
        val payment = Payment(
            createdBy = requireLoggedInUser,
            paidTo = paidTo,
            amount = amount
        )
        group.debtsState = getCalculator(payment).calculateEffectiveDebtForGroup()
        val paymentResponse = addEventUseCase.execute(group, payment)
        _mutableEventsStateFlow.value = eventStateFlow.value.plus(paymentResponse)
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

    private fun getCalculator(withEvent: Event? = null): DebtCalculator {
        val events = eventStateFlow.value
        val payments: List<Payment> = events.filterIsInstance<Payment>().let {
            if (withEvent is Payment)
                it.plus(withEvent)
            else it
        }
        val groupExpenses: List<GroupExpense> = events.filterIsInstance<GroupExpense>().let {
            if (withEvent is GroupExpense)
                it.plus(withEvent)
            else it
        }
        return DebtCalculator(group.peopleState, groupExpenses, payments)
    }

    fun onServiceClicked(subscriptionService: SubscriptionService) {
        emitUiEvent(OnServiceClicked(subscriptionService))
    }
}