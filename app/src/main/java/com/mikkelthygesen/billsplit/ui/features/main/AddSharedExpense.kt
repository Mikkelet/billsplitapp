package com.mikkelthygesen.billsplit.ui.features.main

import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedExpense
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseView
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseViewCallback
import java.util.UUID


interface AddSharedExpenseCallback {
    fun onSharedExpenseUpdate(value: Float)
    fun onParticipantExpenseUpdate(person: Person, value: Float)
    fun onAddSharedExpense(sharedExpense: SharedExpense)
}

@Composable
fun AddSharedExpense(
    participants: List<Person>,
    sharedExpenses: Person,
    addSharedExpenseCallback: AddSharedExpenseCallback
) {
    val expenseViewCallback = object : ExpenseViewCallback {
        override fun onSharedExpenseUpdate(owed: Float) {
            addSharedExpenseCallback.onSharedExpenseUpdate(owed)
        }

        override fun onParticipantExpenseUpdate(person: Person, owed: Float) {
            addSharedExpenseCallback.onParticipantExpenseUpdate(person, owed)
        }

        override fun onRemovePerson(person: Person) {
            // no op
        }

        override fun onFabClick() {
            val sharedExpense = SharedExpense(
                UUID.randomUUID().toString(),
                "",
                sharedExpense = sharedExpenses,
                people = participants
            )
            addSharedExpenseCallback.onAddSharedExpense(sharedExpense)
        }
    }

    ExpenseView(
        participants = participants,
        sharedExpenses = sharedExpenses,
        expenseViewCallback = expenseViewCallback,
        showFab = true
    )
}

