package com.mikkelthygesen.billsplit.ui.features.quick_expense

import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseView
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseViewCallback

interface QuickExpenseViewCallback {
    fun onSharedExpenseUpdate(owed: Float)
    fun onParticipantExpenseUpdate(person: Person, owed: Float)
    fun onRemovePerson(person: ExpenseHolder.IndividualExpenseHolder)
    fun onFabClick()
}

@Composable
fun QuickExpenseView(
    peopleState: List<ExpenseHolder.IndividualExpenseHolder>,
    sharedExpenses: ExpenseHolder.SharedExpenseHolder,
    callback: QuickExpenseViewCallback
) {
    val expenseViewCallback = object : ExpenseViewCallback {
        override fun onSharedExpenseUpdate(owed: Float) {
            callback.onSharedExpenseUpdate(owed)
        }

        override fun onParticipantExpenseUpdate(person: Person, owed: Float) {
            callback.onParticipantExpenseUpdate(person, owed)
        }

        override fun onRemovePerson(person: ExpenseHolder.IndividualExpenseHolder) {
            callback.onRemovePerson(person)
        }

        override fun onFabClick() {
            callback.onFabClick()
        }
    }

    ExpenseView(
        expenseViewCallback = expenseViewCallback,
        expenseHolders = peopleState,
        sharedExpenses = sharedExpenses,
        canEditName = true
    )
}

