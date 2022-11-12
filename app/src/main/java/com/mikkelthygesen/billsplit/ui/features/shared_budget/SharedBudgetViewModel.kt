package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.ui.features.main.AddSharedExpenseCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class SharedBudgetViewModel : ViewModel(), AddSharedExpenseCallback {

    sealed class UiState {
        object ShowBudget : UiState()
        class ShowAddExpense(val sharedExpense: SharedExpense) : UiState()
    }

    private var peopleAdded = 0
    private val sharedExpenses: Person
        get() = mutableParticipantsFlow.value.first()

    private val mutableUiStateFlow = MutableStateFlow<UiState>(UiState.ShowBudget)
    val uiStateFlow: StateFlow<UiState> = mutableUiStateFlow
    private val mutableSharedExpensesStateFlow = MutableStateFlow<List<SharedExpense>>(emptyList())
    val sharedExpensesState: StateFlow<List<SharedExpense>> = mutableSharedExpensesStateFlow
    private val mutableParticipantsFlow =
        MutableStateFlow<List<Person>>(
            listOf(
                getNewSharedExpenses(),
                Person("Person 1", 0F),
                Person("Person 2", 0F),
                Person("Person 3", 0F),
            )
        )
    val peopleState: StateFlow<List<Person>> = mutableParticipantsFlow

    fun addExpense() {
        val sharedExpense = SharedExpense(
            UUID.randomUUID().toString(),
            "",
            getNewSharedExpenses(),
            getResetParticipants()
        )
        mutableUiStateFlow.value = UiState.ShowAddExpense(sharedExpense)
    }

    fun showBudget() {
        mutableUiStateFlow.value = UiState.ShowBudget
    }

    fun addPerson() {
        peopleAdded++
        val person = Person("Person $peopleAdded", 0F)
        val updateList: List<Person> = peopleState.value + listOf(person)
        mutableParticipantsFlow.value = updateList
    }

    override fun onSharedExpenseUpdate(value: Float) {
        sharedExpenses.owed = value
    }

    override fun onParticipantExpenseUpdate(person: Person, value: Float) {
        person.owed = value
    }

    override fun onAddSharedExpense(sharedExpense: SharedExpense) {
        // if expense exists, consider it an edit, else add new expense
        if (!sharedExpensesState.value.contains(sharedExpense)) {
            val updateList = sharedExpensesState.value.plus(sharedExpense)
            mutableSharedExpensesStateFlow.value = updateList
        }
        mutableUiStateFlow.value = UiState.ShowBudget
    }

    fun onRemovePerson(person: Person) {
        val indexOf = peopleState.value.indexOf(person)
        val updateList = peopleState.value.toMutableList()
        updateList.removeAt(indexOf)
        mutableParticipantsFlow.value = updateList
    }

    private fun getResetParticipants(): List<Person> {
        return peopleState.value.map {
            Person(it.name, 0F, it.isParticipant)
        }
    }

    private fun getNewSharedExpenses(): Person = Person("Shared", 0F, false)

    fun editSharedExpense(sharedExpense: SharedExpense) {
        mutableUiStateFlow.value = UiState.ShowAddExpense(sharedExpense)
    }
}