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

    object Events : UiState
    object ShowDebt : UiState
    class ShowExpense(val groupExpense: GroupExpense) : UiState
    object ShowAddPersonDialog : DialogState
    class ConfirmChangesDialog(val groupExpense: GroupExpense) : DialogState

    private val _people = mutableListOf<Person>()
    val people: List<Person> = _people
    private lateinit var group: Group

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Events)
    private val _mutableEventsStateFlow = MutableStateFlow<List<Event>>(emptyList())
    val eventStateFlow: StateFlow<List<Event>> = _mutableEventsStateFlow

    fun getGroup(groupId: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            println("qqq retrievingId=$groupId")
            val response = kotlin.runCatching { api.getGroup(groupId) }
            response.fold(
                onSuccess = {
                    println("qqq getGroup=${it.toGroup()}")
                    group = it.toGroup()
                    _people.addAll(group.peopleState)
                    _mutableEventsStateFlow.value = group.events
                    updateUiState(Events)
                },
                onFailure = Timber::e
            )
        }
    }

    fun addExpense() {
        val groupExpense = GroupExpense(
            id = group.id,
            createdBy = people.first(), // TODO get logged in
            description = "",
            payee = people.first(),
            sharedExpense = 0F,
            individualExpenses = people.toNewIndividualExpenses(),
        )
        _mutableUiStateFlow.value = ShowExpense(groupExpense)
    }

    fun addPayment(payment: Payment) {
        viewModelScope.launch {
            val response = kotlin.runCatching { api.addEvent(group.id, payment) }
            response.fold(
                onSuccess = {
                    _mutableEventsStateFlow.value = eventStateFlow.value.plus(payment)
                },
                onFailure = {
                    println("qqq error submitting payment $it")
                }
            )
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
                        _mutableEventsStateFlow.value = eventStateFlow.value.plus(groupExpense)
                    },
                    onFailure = {
                        println("qqq error submitting $it")
                    }
                )
            }
        } else if (originalCopy != groupExpense) {
            val updatedCopy = groupExpense
                .copy(individualExpenses = groupExpense.individualExpenses.map { it.copy() })
            val groupExpensesChanged = GroupExpensesChanged(
                id = originalCopy.id,
                createdBy = originalCopy.createdBy, // TODO getLoggedIn
                groupExpenseOriginal = originalCopy,
                groupExpenseEdited = updatedCopy
            )
            viewModelScope.launch {
                val response = runCatching { api.addEvent(group.id, groupExpensesChanged) }
                response.fold(
                    onSuccess = {
                        _mutableEventsStateFlow.value =
                            eventStateFlow.value.plus(groupExpensesChanged)
                    },
                    onFailure = {
                        println("qqq error submitting $it")
                    }
                )
            }
        }
        showEvents()
    }

    fun showConfirmChangesDialog(groupExpense: GroupExpense) {
        _mutableDialogStateFlow.value = ConfirmChangesDialog(groupExpense)
    }

    fun showDebt() {
        updateUiState(ShowDebt)
    }

    fun editSharedExpense(sharedExpense: GroupExpense) {
        updateUiState(ShowExpense(sharedExpense))
    }

    fun showEvents() {
        updateUiState(Events)
    }

    fun getLoggedIn() = people.first()
}