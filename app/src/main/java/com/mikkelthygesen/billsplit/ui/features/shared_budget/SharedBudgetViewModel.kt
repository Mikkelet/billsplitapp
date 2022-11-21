package com.mikkelthygesen.billsplit.ui.features.shared_budget

import android.text.format.DateUtils
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.models.ExpenseHolder.IndividualExpenseHolder
import com.mikkelthygesen.billsplit.models.ExpenseHolder.SharedExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.Payment
import java.util.*
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.interfaces.IShareable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.Instant

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
    val paymentsFlow: StateFlow<List<Payment>> = _payments

    private val _mutableUiStateFlow = MutableStateFlow<UiState>(UiState.ShowBudget)
    val uiStateFlow: StateFlow<UiState> = _mutableUiStateFlow

    private val _mutableSharedExpensesStateFlow = MutableStateFlow<List<GroupExpense>>(emptyList())
    val sharedExpensesState: StateFlow<List<GroupExpense>> = _mutableSharedExpensesStateFlow

    private val _mutableDialogStateFlow = MutableStateFlow<DialogState>(DialogState.DismissDialogs)
    val dialogState: StateFlow<DialogState> = _mutableDialogStateFlow

    private val _mutableUiEventsStateFlow = MutableSharedFlow<UiEvent>()
    val uiEventsState: SharedFlow<UiEvent> = _mutableUiEventsStateFlow

    private val _people = mutableListOf<Person>()
    val people: List<Person> = _people

    fun shareableStateFlow(): SharedFlow<List<IShareable>> =
        sharedExpensesState.combine(paymentsFlow as StateFlow<List<IShareable>>) { x, y ->
            x + y
        }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())


    init {
        _people.addAll((1..3).map { Person("id-$it", "Person $it") })
    }

    fun addExpense() {
        val resetParticipants = getResetParticipants()
        val sharedExpense = GroupExpense(
            id = UUID.randomUUID().toString(),
            description = "",
            payee = resetParticipants[0],
            sharedExpense = getNewSharedExpenses(),
            individualExpenses = resetParticipants,
        )
        _mutableUiStateFlow.value = UiState.ShowAddExpense(sharedExpense)
    }

    fun addPayment(payment: Payment) {
        _payments.value = paymentsFlow.value.plus(payment)
    }

    fun addPerson(name: String) {
        val pId = (people.size + 1) + 1
        val person = Person("id$pId", name)
        _people.add(person)
    }

    fun saveGroupExpense(sharedExpense: GroupExpense) {
        // if expense exists, consider it an edit, else add new expense
        sharedExpense.saveChanges()
        if (!sharedExpensesState.value.contains(sharedExpense)) {
            _mutableSharedExpensesStateFlow.value = sharedExpensesState.value.plus(sharedExpense)
        }
        _mutableUiStateFlow.value = UiState.ShowBudget
    }

    fun onRemoveHolder(person: Person) {
        val indexOf = people.indexOf(person)
        _people.removeAt(indexOf)
    }

    private fun getResetParticipants(): List<IndividualExpenseHolder> {
        return people.map { IndividualExpenseHolder(it, 0F, true) }
    }

    private fun getNewSharedExpenses(): SharedExpenseHolder =
        SharedExpenseHolder(0F)

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