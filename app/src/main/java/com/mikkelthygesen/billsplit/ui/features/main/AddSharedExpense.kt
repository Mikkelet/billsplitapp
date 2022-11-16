package com.mikkelthygesen.billsplit.ui.features.main

import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.ui.features.shared_budget.GroupExpense
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseView
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseViewCallback


interface AddSharedExpenseCallback {
    fun onSharedExpenseUpdate(value: Float)
    fun onParticipantExpenseUpdate(person: Person, value: Float)
    fun onAddSharedExpense(sharedExpense: GroupExpense)
}

@Composable
fun AddSharedExpense(
    sharedExpense: GroupExpense,
    addSharedExpenseCallback: AddSharedExpenseCallback
) {
    val expenseViewCallback = object : ExpenseViewCallback {
        override fun onSharedExpenseUpdate(owed: Float) {
            addSharedExpenseCallback.onSharedExpenseUpdate(owed)
        }

        override fun onParticipantExpenseUpdate(person: Person, owed: Float) {
            addSharedExpenseCallback.onParticipantExpenseUpdate(person, owed)
        }

        override fun onRemovePerson(person: ExpenseHolder.IndividualExpenseHolder) {
            // no op
        }

        override fun onFabClick() {
            addSharedExpenseCallback.onAddSharedExpense(sharedExpense)
        }
    }

    ExpenseView(
        expenseHolders = sharedExpense.individualExpenses,
        sharedExpenses = sharedExpense.sharedExpense,
        expenseViewCallback = expenseViewCallback,
        showFab = true
    )
}

