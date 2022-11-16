package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.lifecycle.ViewModel
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.models.ExpenseHolder.IndividualExpenseHolder
import com.mikkelthygesen.billsplit.models.ExpenseHolder.SharedExpenseHolder
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.ui.features.main.AddSharedExpenseCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class SharedBudgetViewModel : ViewModel(), AddSharedExpenseCallback {

    sealed class UiState {
        object ShowBudget : UiState()
        class ShowAddExpense(val sharedExpense: GroupExpense) : UiState()
    }

    private var peopleAdded = 0
    val sharedExpenses: SharedExpenseHolder = SharedExpenseHolder(0F)

    private val mutableUiStateFlow = MutableStateFlow<UiState>(UiState.ShowBudget)
    val uiStateFlow: StateFlow<UiState> = mutableUiStateFlow
    private val mutableSharedExpensesStateFlow = MutableStateFlow<List<GroupExpense>>(emptyList())
    val sharedExpensesState: StateFlow<List<GroupExpense>> = mutableSharedExpensesStateFlow
    private val mutableParticipantsFlow =
        MutableStateFlow<List<IndividualExpenseHolder>>(
            listOf(
                IndividualExpenseHolder("Person 1", 0F),
                IndividualExpenseHolder("Person 2", 0F),
                IndividualExpenseHolder("Person 3", 0F)
            )
        )
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

    fun addPerson() {
        peopleAdded++
        val person = IndividualExpenseHolder("Person $peopleAdded", 0F)
        val updateList: List<IndividualExpenseHolder> = peopleState.value + listOf(person)
        mutableParticipantsFlow.value = updateList
    }

    override fun onSharedExpenseUpdate(value: Float) {
        sharedExpenses.expense = value
    }

    override fun onParticipantExpenseUpdate(person: Person, value: Float) {
        person.owed = value
    }

    override fun onAddSharedExpense(sharedExpense: GroupExpense) {
        // if expense exists, consider it an edit, else add new expense
        if (!sharedExpensesState.value.contains(sharedExpense)) {
            val updateList = sharedExpensesState.value.plus(sharedExpense)
            mutableSharedExpensesStateFlow.value = updateList
        }
        mutableUiStateFlow.value = UiState.ShowBudget
    }

    fun onExpenseHolder(individualExpenseHolder: IndividualExpenseHolder) {
        val indexOf = peopleState.value.indexOf(individualExpenseHolder)
        val updateList = peopleState.value.toMutableList()
        updateList.removeAt(indexOf)
        mutableParticipantsFlow.value = updateList
    }

    private fun getResetParticipants(): List<IndividualExpenseHolder> {
        return peopleState.value.map {
            IndividualExpenseHolder(it.name, 0F, it.isParticipant)
        }
    }

    private fun getNewSharedExpenses(): ExpenseHolder.SharedExpenseHolder =
        ExpenseHolder.SharedExpenseHolder(0F)

    fun editSharedExpense(sharedExpense: GroupExpense) {
        mutableUiStateFlow.value = UiState.ShowAddExpense(sharedExpense)
    }
}