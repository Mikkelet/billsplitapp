package com.mikkelthygesen.billsplit.ui.features.quick_expense

import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseView
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseViewCallback

interface QuickExpenseViewCallback {
    fun onSharedExpenseUpdate(expense: Float)
    fun onParticipantExpenseUpdate(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder, owed: Float)
    fun onRemovePerson(person: ExpenseHolder.IndividualExpenseHolder)
}

@Composable
fun QuickExpenseView(
    groupExpense: GroupExpense,
    callback: QuickExpenseViewCallback
) {
    val expenseViewCallback = object : ExpenseViewCallback {
        override fun onSharedExpenseUpdate(expense: Float) {
            callback.onSharedExpenseUpdate(expense)
        }

        override fun onParticipantExpenseUpdate(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder, expense: Float) {
            callback.onParticipantExpenseUpdate(individualExpenseHolder, expense)
        }

        override fun onRemovePerson(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder) {
            callback.onRemovePerson(individualExpenseHolder)
        }
    }

    ExpenseView(
        expenseViewCallback = expenseViewCallback,
        groupExpense = groupExpense
    )
}

