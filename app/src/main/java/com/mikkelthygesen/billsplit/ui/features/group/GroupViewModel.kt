package com.mikkelthygesen.billsplit.ui.features.group

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.interfaces.IShareable
import com.mikkelthygesen.billsplit.samplePayments
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import com.mikkelthygesen.billsplit.toNewIndividualExpenses
import kotlinx.coroutines.flow.*
import java.util.*

class GroupViewModel() : BaseViewModel() {

    object Events : UiState
    object ShowDebt : UiState
    class ShowExpense(val groupExpense: GroupExpense) : UiState
    object ShowAddPersonDialog : DialogState
    class ConfirmChangesDialog(val groupExpense: GroupExpense) : DialogState

    private val _people = mutableListOf<Person>(*samplePeopleShera.toTypedArray())
    val people: List<Person> = _people

    private val _payments = MutableStateFlow(samplePayments)
    val paymentsStateFlow: StateFlow<List<Payment>> = _payments

    private val _mutableSharedExpensesStateFlow = MutableStateFlow(sampleSharedExpenses)
    val sharedExpensesStateFlow: StateFlow<List<GroupExpense>> = _mutableSharedExpensesStateFlow

    private val _mutableChangesStateFlow = MutableStateFlow<List<GroupExpensesChanged>>(emptyList())
    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(Events)

    fun addExpense() {
        val sharedExpense = GroupExpense(
            id = UUID.randomUUID().toString(),
            createdBy = people.first(), // TODO get logged in
            description = "",
            payee = people.first(),
            sharedExpense = 0F,
            individualExpenses = people.toNewIndividualExpenses(),
        )
        _mutableUiStateFlow.value = ShowExpense(sharedExpense)
    }

    fun shareableStateFlow(): SharedFlow<List<IShareable>> =
        combine(
            sharedExpensesStateFlow,
            paymentsStateFlow,
            _mutableChangesStateFlow
        ) { groupExpenses, payments, changes ->
            groupExpenses + payments + changes
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())


    fun addPayment(payment: Payment) {
        _payments.value = paymentsStateFlow.value.plus(payment)
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
        if (!sharedExpensesStateFlow.value.contains(groupExpense)) {
            _mutableSharedExpensesStateFlow.value = sharedExpensesStateFlow.value.plus(groupExpense)
        } else if (originalCopy != groupExpense) {
            val updatedCopy = groupExpense
                .copy(individualExpenses = groupExpense.individualExpenses.map { it.copy() })
            val groupExpensesChanged = GroupExpensesChanged(
                createdBy = originalCopy.createdBy, // TODO getLoggedIn
                groupExpenseOriginal = originalCopy,
                groupExpenseEdited = updatedCopy
            )
            _mutableChangesStateFlow.value =
                _mutableChangesStateFlow.value.plus(groupExpensesChanged)
        }
        _mutableUiStateFlow.value = Events
    }

    fun showConfirmChangesDialog(groupExpense: GroupExpense) {
        _mutableDialogStateFlow.value = ConfirmChangesDialog(groupExpense)
    }

    fun showDebt() {
        _mutableUiStateFlow.value = ShowDebt
    }

    fun editSharedExpense(sharedExpense: GroupExpense) {
        _mutableUiStateFlow.value = ShowExpense(sharedExpense)
    }

    fun showEvents() {
        _mutableUiStateFlow.value = Events
    }

    fun getLoggedIn() = people.first()
}