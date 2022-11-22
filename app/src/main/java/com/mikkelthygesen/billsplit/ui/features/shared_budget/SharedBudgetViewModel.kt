package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.models.IndividualExpense
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment
import java.util.*
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.interfaces.IShareable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SharedBudgetViewModel : ViewModel() {

    sealed class UiState {
        object ShowBudget : UiState()
        class ShowAddExpense(val sharedExpense: GroupExpense) : UiState()
        object ViewExpense : UiState()
    }

    sealed class DialogState {
        object ShowAddPersonDialog : DialogState()
        class ShowConfirmEditDialog(val groupExpense: GroupExpense) : DialogState()
        object DismissDialogs : DialogState()
    }

    sealed class UiEvent {
        object OnBackPressed : UiEvent()
    }

    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val paymentsStateFlow: StateFlow<List<Payment>> = _payments

    private val _mutableUiStateFlow = MutableStateFlow<UiState>(UiState.ShowBudget)
    val uiStateFlow: StateFlow<UiState> = _mutableUiStateFlow

    private val _mutableSharedExpensesStateFlow = MutableStateFlow<List<GroupExpense>>(emptyList())
    val sharedExpensesStateFlow: StateFlow<List<GroupExpense>> = _mutableSharedExpensesStateFlow

    private val _mutableChangesStateFlow = MutableStateFlow<List<GroupExpensesChanged>>(emptyList())

    private val _mutableDialogStateFlow = MutableStateFlow<DialogState>(DialogState.DismissDialogs)
    val dialogState: StateFlow<DialogState> = _mutableDialogStateFlow

    private val _mutableUiEventsStateFlow = MutableSharedFlow<UiEvent>()
    val uiEventsState: SharedFlow<UiEvent> = _mutableUiEventsStateFlow

    private val _people = mutableListOf<Person>()
    val people: List<Person> = _people

    fun shareableStateFlow(): SharedFlow<List<IShareable>> =
        combine(
            sharedExpensesStateFlow,
            paymentsStateFlow,
            _mutableChangesStateFlow
        ) { groupExpenses, payments, changes ->
            groupExpenses + payments + changes
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    init {
        _people.addAll((1..3).map { Person("id-$it", "Person $it") })
    }

    fun addExpense() {
        val resetParticipants = getResetParticipants()
        val sharedExpense = GroupExpense(
            id = UUID.randomUUID().toString(),
            createdBy = people.first(),
            description = "",
            payee = people.first(),
            sharedExpense = 0F,
            individualExpenses = resetParticipants,
        )
        _mutableUiStateFlow.value = UiState.ShowAddExpense(sharedExpense)
    }

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
        val originalCopy =
            groupExpense.copy(individualExpenses = groupExpense.individualExpenses.map { it.copy() })
        // save expenses
        groupExpense.saveChanges()
        // if expense doesn't appear in list, add it, else assume it's an edit and log the change
        if (!sharedExpensesStateFlow.value.contains(groupExpense)) {
            _mutableSharedExpensesStateFlow.value = sharedExpensesStateFlow.value.plus(groupExpense)
        } else if (originalCopy != groupExpense) {
            val updatedCopy =
                groupExpense.copy(individualExpenses = groupExpense.individualExpenses.map { it.copy() })
            val groupExpensesChanged = GroupExpensesChanged(getLoggedIn(), originalCopy, updatedCopy)
            _mutableChangesStateFlow.value =
                _mutableChangesStateFlow.value.plus(groupExpensesChanged)
        }
        _mutableUiStateFlow.value = UiState.ShowBudget
    }

    fun onRemoveHolder(person: Person) {
        val indexOf = people.indexOf(person)
        _people.removeAt(indexOf)
    }

    private fun getResetParticipants(): List<IndividualExpense> {
        return people.map { IndividualExpense(it, 0F, true) }
    }

    fun showBudget() {
        _mutableUiStateFlow.value = UiState.ShowBudget
    }

    fun showExpenses() {
        _mutableUiStateFlow.value = UiState.ViewExpense
    }

    fun editSharedExpense(sharedExpense: GroupExpense) {
        _mutableUiStateFlow.value = UiState.ShowAddExpense(sharedExpense)
    }

    fun dismissDialogs() {
        _mutableDialogStateFlow.value = DialogState.DismissDialogs
    }

    fun onBackButtonPressed() {
        viewModelScope.launch {
            _mutableUiEventsStateFlow.emit(UiEvent.OnBackPressed)
        }
    }

    fun showConfirmChangesDialog(groupExpense: GroupExpense) {
        _mutableDialogStateFlow.value = DialogState.ShowConfirmEditDialog(groupExpense)
    }

    fun showAddPersonDialog() {
        _mutableDialogStateFlow.value = DialogState.ShowAddPersonDialog
    }

    fun getLoggedIn(): Person = people[1]
}