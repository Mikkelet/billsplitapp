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
import com.mikkelthygesen.billsplit.domain.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import com.mikkelthygesen.billsplit.domain.usecases.*
import com.mikkelthygesen.billsplit.toNewIndividualExpenses
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val addEventUseCase: AddEventUseCase,
    private val getGroupUseCase: GetGroupUseCase,
    private val getServicesFromLocalUseCase: GetServicesFromLocalUseCase
) : BaseViewModel() {

    object Chat : UiState
    object ShowDebt : UiState
    object Services : UiState
    data class AddService(val subscriptionService: SubscriptionService) : UiState
    class EditExpense(val groupExpense: GroupExpense) : UiState
    class ConfirmChangesDialog(val groupExpense: GroupExpense) : DialogState
    object SaveServiceClicked : UiEvent
    data class OnServiceClicked(val service: SubscriptionService) : UiEvent
    object ServiceSaved : UiEvent
    object SaveServiceFailed : UiEvent

    var showChatLoader by mutableStateOf(false)
        private set

    private val _people = mutableListOf<Person>()
    val people: List<Person> = _people
    lateinit var group: Group
        private set

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Chat)
    private val _mutableEventsStateFlow = MutableStateFlow<List<Event>>(emptyList())
    val eventStateFlow: StateFlow<List<Event>> = _mutableEventsStateFlow
    private val _mutableServicesStateFlow = MutableStateFlow<List<SubscriptionService>>(emptyList())

    fun getGroup(groupId: String) {
        if (this::group.isInitialized) return
        viewModelScope.launch {
            updateUiState(UiState.Loading)
            // call get cached group
            val cacheResponse = runCatching {
                getGroupUseCase.execute(groupId, false)
            }
            cacheResponse.foldSuccess { group ->
                this@GroupViewModel.group = group
                _people.addAll(group.peopleState)
                _mutableEventsStateFlow.value = group.events
                _mutableServicesStateFlow.value = group.services
                updateUiState(Chat)
            }

            // then sync with remote
            val syncResponse = runCatching {
                showChatLoader = true
                getGroupUseCase.execute(groupId, true)
            }
            syncResponse.foldSuccess { group ->
                _people.clear()
                _people.addAll(group.peopleState)
                _mutableEventsStateFlow.value = group.events
                _mutableServicesStateFlow.value = group.services
            }
            showChatLoader = false
        }
    }

    suspend fun getLocalServices(): List<SubscriptionService> =
        getServicesFromLocalUseCase.execute(group.id)

    fun addExpense() {
        requireLoggedInUser {
            val groupExpense = GroupExpense(
                id = group.id,
                createdBy = it,
                description = "",
                payee = it,
                sharedExpense = 0F,
                individualExpenses = people.toNewIndividualExpenses(),
            )
            _mutableUiStateFlow.value = EditExpense(groupExpense)
        }
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

    fun addPerson(name: String) {
        val pId = (people.size + 1) + 1
        val person = Person("id$pId", name)
        _people.add(person)
    }

    fun saveGroupExpense(groupExpense: GroupExpense) {
        updateUiState(UiState.Loading)
        // save original in case of edit
        val originalCopy = groupExpense.copy()
        // apply changes
        groupExpense.saveChanges()
        // if expense doesn't appear in list, add it, else assume it's an edit and log the change
        if (!eventStateFlow.value.contains(groupExpense)) {
            handleNewExpense(groupExpense)
        } else if (originalCopy != groupExpense) {
            handleEditExpense(originalCopy, groupExpense)
        }
    }

    private fun handleNewExpense(groupExpense: GroupExpense) {
        viewModelScope.launch {
            val response = runCatching {
                group.debtsState = getCalculator(groupExpense).calculateEffectiveDebtForGroup()
                addEventUseCase.execute(group, groupExpense)
            }
            response.foldSuccess {
                _mutableEventsStateFlow.value = eventStateFlow.value.plus(it)
                showChat()
            }
        }
    }

    private fun handleEditExpense(originalCopy: GroupExpense, updatedCopy: GroupExpense) {
        viewModelScope.launch {
            val groupExpensesChanged = GroupExpensesChanged(
                id = "",
                createdBy = requireLoggedInUser,
                groupExpenseOriginal = originalCopy,
                groupExpenseEdited = updatedCopy
            )
            val response = runCatching {
                group.debtsState = getCalculator().calculateEffectiveDebtForGroup()
                addEventUseCase.execute(group, groupExpensesChanged)
            }
            response.foldSuccess {
                _mutableEventsStateFlow.value = eventStateFlow.value.plus(it)
                showChat()
            }
        }
    }

    fun showConfirmChangesDialog(groupExpense: GroupExpense) {
        showDialog(ConfirmChangesDialog(groupExpense))
    }

    fun showDebt() {
        updateUiState(ShowDebt)
    }

    fun showServices() {
        updateUiState(Services)
    }

    fun editSharedExpense(sharedExpense: GroupExpense) {
        updateUiState(EditExpense(sharedExpense))
    }

    fun showChat() {
        updateUiState(Chat)
    }

    fun handleBack(): Boolean {
        return when (val uiState = uiStateFlow.value) {
            is EditExpense -> {
                if (uiState.groupExpense.isChanged())
                    showConfirmChangesDialog(uiState.groupExpense)
                else showChat()
                true
            }
            is AddService -> {
                showServices()
                true
            }
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

    fun addServiceClicked() {
        emitUiEvent(SaveServiceClicked)
    }
}