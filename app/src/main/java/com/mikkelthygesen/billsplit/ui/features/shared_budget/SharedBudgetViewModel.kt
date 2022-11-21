package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.models.ExpenseHolder.IndividualExpenseHolder
import com.mikkelthygesen.billsplit.models.ExpenseHolder.SharedExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import com.mikkelthygesen.billsplit.models.Person
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SharedBudgetViewModel : ViewModel() {

    sealed class UiState {
        object ShowBudget : UiState()
        class ShowAddExpense(val sharedExpense: GroupExpense) : UiState()
        class ViewExpense(val people: List<IndividualExpenseHolder>) : UiState()
    }

    sealed class DialogState {
        object ShowAddPersonDialog : DialogState()
        class ShowConfirmEditDialog(val groupExpense: GroupExpense) : DialogState()
        object DismissDialogs : DialogState()
    }

    sealed class UiEvent {
        object OnBackPressed : UiEvent()
    }

    private val people = (1..10).map { Person("id-$it", "Person $it") }
    private var peopleAdded = people.size

    private val mutableUiStateFlow = MutableStateFlow<UiState>(UiState.ShowBudget)
    val uiStateFlow: StateFlow<UiState> = mutableUiStateFlow

    private val mutableSharedExpensesStateFlow = MutableStateFlow<List<GroupExpense>>(emptyList())
    val sharedExpensesState: StateFlow<List<GroupExpense>> = mutableSharedExpensesStateFlow

    private val mutableDialogStateFlow = MutableStateFlow<DialogState>(DialogState.DismissDialogs)
    val dialogState: StateFlow<DialogState> = mutableDialogStateFlow

    private val mutableUiEventsStateFlow = MutableSharedFlow<UiEvent>()
    val uiEventsState: SharedFlow<UiEvent> = mutableUiEventsStateFlow

    private val mutableParticipantsFlow =
        MutableStateFlow<List<IndividualExpenseHolder>>(people.map {
            IndividualExpenseHolder(it, 0F)
        })
    val peopleState: StateFlow<List<IndividualExpenseHolder>> = mutableParticipantsFlow

    fun addExpense() {
        val resetParticipants = getResetParticipants()
        val sharedExpense = GroupExpense(
            id = UUID.randomUUID().toString(),
            description = "",
            payee = resetParticipants[0],
            sharedExpense = getNewSharedExpenses(),
            individualExpenses = resetParticipants
        )
        mutableUiStateFlow.value = UiState.ShowAddExpense(sharedExpense)
    }

    fun addPerson() {
        peopleAdded++
        val person = IndividualExpenseHolder(Person("id$peopleAdded", "Person $peopleAdded"), 0F)
        val updateList: List<IndividualExpenseHolder> = peopleState.value.plus(person)
        mutableParticipantsFlow.value = updateList
    }

    fun addPerson(name: String) {
        peopleAdded++
        val person = IndividualExpenseHolder(Person("id$peopleAdded", name), 0F)
        val updateList: List<IndividualExpenseHolder> = peopleState.value.plus(person)
        mutableParticipantsFlow.value = updateList

    }

    fun saveGroupExpense(sharedExpense: GroupExpense) {
        // if expense exists, consider it an edit, else add new expense
        sharedExpense.saveChanges()
        if (!sharedExpensesState.value.contains(sharedExpense)) {
            val updateList = sharedExpensesState.value.plus(sharedExpense)
            mutableSharedExpensesStateFlow.value = updateList
        }
        mutableUiStateFlow.value = UiState.ShowBudget
    }

    fun onRemoveExpenseHolder(individualExpenseHolder: IndividualExpenseHolder) {
        val indexOf = peopleState.value.indexOf(individualExpenseHolder)
        val updateList = peopleState.value.toMutableList()
        updateList.removeAt(indexOf)
        mutableParticipantsFlow.value = updateList
    }

    private fun getResetParticipants(): List<IndividualExpenseHolder> {
        return peopleState.value.map { it.reset() }
    }

    private fun getNewSharedExpenses(): SharedExpenseHolder =
        SharedExpenseHolder(0F)

    fun showBudget() {
        mutableUiStateFlow.value = UiState.ShowBudget
    }

    fun showExpenses() {
        mutableUiStateFlow.value = UiState.ViewExpense(peopleState.value)
    }

    fun editSharedExpense(sharedExpense: GroupExpense) {
        mutableUiStateFlow.value = UiState.ShowAddExpense(sharedExpense)
    }

    fun dismissDialogs() {
        mutableDialogStateFlow.value = DialogState.DismissDialogs
    }

    fun onBackButtonPressed() {
        viewModelScope.launch {
            mutableUiEventsStateFlow.emit(UiEvent.OnBackPressed)
        }
    }

    fun showConfirmChangesDialog(groupExpense: GroupExpense) {
        mutableDialogStateFlow.value = DialogState.ShowConfirmEditDialog(groupExpense)
    }

    fun showAddPersonDialog() {
        mutableDialogStateFlow.value = DialogState.ShowAddPersonDialog
    }

    fun getLoggedIn(): Person = people[1]
}