package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.lifecycle.ViewModel
import com.mikkelthygesen.billsplit.models.ExpenseHolder.IndividualExpenseHolder
import com.mikkelthygesen.billsplit.models.ExpenseHolder.SharedExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import com.mikkelthygesen.billsplit.models.Person

class SharedBudgetViewModel : ViewModel() {

    sealed class UiState {
        object ShowBudget : UiState()
        class ShowAddExpense(val sharedExpense: GroupExpense) : UiState()
        class ViewExpense(val people: List<IndividualExpenseHolder>) : UiState()
    }

    private val sharedExpenses: SharedExpenseHolder = SharedExpenseHolder(0F)

    private val mutableUiStateFlow = MutableStateFlow<UiState>(UiState.ShowBudget)
    val uiStateFlow: StateFlow<UiState> = mutableUiStateFlow
    private val mutableSharedExpensesStateFlow = MutableStateFlow<List<GroupExpense>>(emptyList())
    private val people = (1..10).map { Person("id-$it", "Person $it") }
    private var peopleAdded = people.size
    val sharedExpensesState: StateFlow<List<GroupExpense>> = mutableSharedExpensesStateFlow
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
            payee = resetParticipants[1],
            sharedExpense = getNewSharedExpenses(),
            individualExpenses = resetParticipants
        )
        mutableUiStateFlow.value = UiState.ShowAddExpense(sharedExpense)
    }

    fun showBudget() {
        mutableUiStateFlow.value = UiState.ShowBudget
    }

    fun showExpenses() {
        mutableUiStateFlow.value = UiState.ViewExpense(peopleState.value)
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

    fun onSharedExpenseUpdate(value: Float) {
        sharedExpenses.expenseState = value
    }

    fun onParticipantExpenseUpdate(
        individualExpenseHolder: IndividualExpenseHolder,
        value: Float
    ) {
        individualExpenseHolder.expenseState = value
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
        return peopleState.value.map {
            IndividualExpenseHolder(it.person, 0F, it.isParticipantState)
        }
    }

    private fun getNewSharedExpenses(): SharedExpenseHolder =
        SharedExpenseHolder(0F)

    fun editSharedExpense(sharedExpense: GroupExpense) {
        mutableUiStateFlow.value = UiState.ShowAddExpense(sharedExpense)
    }

    fun getLoggedIn(): Person = people[1]
}