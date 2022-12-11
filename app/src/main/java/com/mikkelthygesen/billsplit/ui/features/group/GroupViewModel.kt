package com.mikkelthygesen.billsplit.ui.features.group

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.data.network.ServerApiImpl
import com.mikkelthygesen.billsplit.models.*
import com.mikkelthygesen.billsplit.models.interfaces.Event
import com.mikkelthygesen.billsplit.toNewIndividualExpenses
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor() : BaseViewModel() {

    private val api = ServerApiImpl()

    object Expenses : UiState
    class ShowDebt(val user: Person) : UiState
    class EditExpense(val groupExpense: GroupExpense) : UiState
    class ConfirmChangesDialog(val groupExpense: GroupExpense) : DialogState

    private val _people = mutableListOf<Person>()
    val people: List<Person> = _people
    lateinit var group: Group
        private set

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Expenses)
    private val _mutableEventsStateFlow = MutableStateFlow<List<Event>>(emptyList())
    val eventStateFlow: StateFlow<List<Event>> = _mutableEventsStateFlow

    fun getGroup(groupId: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val response = kotlin.runCatching { api.getGroup(groupId) }
            response.fold(
                onSuccess = { group ->
                    this@GroupViewModel.group = group
                    _people.addAll(group.peopleState)
                    _mutableEventsStateFlow.value = group.events
                    updateUiState(Expenses)
                },
                onFailure = Timber::e
            )
        }
    }

    fun addExpense() {
        checkAuthStatus {
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
        checkAuthStatusAsync {
            val payment = Payment(
                createdBy = it,
                paidTo = paidTo,
                amount = amount
            )
            val paymentResponse = api.addEvent(group.id, payment)
            _mutableEventsStateFlow.value = eventStateFlow.value.plus(paymentResponse)
        }
    }

    fun addPerson(name: String) {
        val pId = (people.size + 1) + 1
        val person = Person("id$pId", name)
        _people.add(person)
    }

    fun saveGroupExpense(groupExpense: GroupExpense) {
        // save original in case of edit
        val originalCopy = groupExpense
            .copy(individualExpenses = groupExpense.individualExpenses.map { it.copy() })
        // save expenses
        groupExpense.saveChanges()
        // if expense doesn't appear in list, add it, else assume it's an edit and log the change
        if (!eventStateFlow.value.contains(groupExpense)) {
            viewModelScope.launch {
                val response = kotlin.runCatching { api.addEvent(groupExpense.id, groupExpense) }
                response.fold(
                    onSuccess = {
                        _mutableEventsStateFlow.value = eventStateFlow.value.plus(it)
                    },
                    onFailure = ::println
                )
            }
        } else if (originalCopy != groupExpense) {
            val updatedCopy = groupExpense
                .copy(individualExpenses = groupExpense.individualExpenses.map { it.copy() })
            val groupExpensesChanged = GroupExpensesChanged(
                id = originalCopy.id,
                createdBy = originalCopy.createdBy,
                groupExpenseOriginal = originalCopy,
                groupExpenseEdited = updatedCopy
            )
            viewModelScope.launch {
                val response = runCatching { api.addEvent(group.id, groupExpensesChanged) }
                response.fold(
                    onSuccess = {
                        _mutableEventsStateFlow.value = eventStateFlow.value.plus(it)
                    },
                    onFailure = ::println
                )
            }
        }
        showEvents()
    }

    fun showConfirmChangesDialog(groupExpense: GroupExpense) {
        _mutableDialogStateFlow.value = ConfirmChangesDialog(groupExpense)
    }

    fun showDebt() {
        checkAuthStatus {
            updateUiState(ShowDebt(it))
        }
    }

    fun editSharedExpense(sharedExpense: GroupExpense) {
        updateUiState(EditExpense(sharedExpense))
    }

    fun showEvents() {
        updateUiState(Expenses)
    }
}