package com.mikkelthygesen.billsplit.ui.features.quick_expense

import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseView
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseViewCallback

interface QuickExpenseViewCallback {
    fun onSharedExpenseUpdate(owed: Float)
    fun onParticipantExpenseUpdate(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder, owed: Float)
    fun onRemovePerson(person: ExpenseHolder.IndividualExpenseHolder)
    fun onFabClick()
}

@Composable
fun QuickExpenseView(
    groupExpense: GroupExpense,
    callback: QuickExpenseViewCallback
) {
    val expenseViewCallback = object : ExpenseViewCallback {
        override fun onSharedExpenseUpdate(owed: Float) {
            callback.onSharedExpenseUpdate(owed)
        }

        override fun onParticipantExpenseUpdate(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder, owed: Float) {
            callback.onParticipantExpenseUpdate(individualExpenseHolder, owed)
        }

        override fun onRemovePerson(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder) {
            callback.onRemovePerson(individualExpenseHolder)
        }

        override fun onPayeeSelected(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder) {

        }

        override fun onFabClick() {
            callback.onFabClick()
        }
    }

    ExpenseView(
        expenseViewCallback = expenseViewCallback,
        groupExpense = groupExpense
    )
}

