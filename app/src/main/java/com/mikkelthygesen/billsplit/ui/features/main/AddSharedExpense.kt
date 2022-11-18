package com.mikkelthygesen.billsplit.ui.features.main

import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseView
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseViewCallback


interface AddSharedExpenseCallback {
    fun onSharedExpenseUpdate(value: Float)
    fun onParticipantExpenseUpdate(
        individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder,
        value: Float
    )

    fun onAddSharedExpense(sharedExpense: GroupExpense)
    fun onPayeeSelected(
        groupExpense: GroupExpense,
        individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder
    )
}

@Composable
fun AddSharedExpense(
    groupExpense: GroupExpense,
    addSharedExpenseCallback: AddSharedExpenseCallback
) {
    val expenseViewCallback = object : ExpenseViewCallback {
        override fun onSharedExpenseUpdate(owed: Float) {
            addSharedExpenseCallback.onSharedExpenseUpdate(owed)
        }

        override fun onParticipantExpenseUpdate(
            individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder,
            owed: Float
        ) {
            addSharedExpenseCallback.onParticipantExpenseUpdate(individualExpenseHolder, owed)
        }

        override fun onRemovePerson(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder) =
            Unit

        override fun onPayeeSelected(
            individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder
        ) = addSharedExpenseCallback.onPayeeSelected(groupExpense, individualExpenseHolder)

        override fun onFabClick() = addSharedExpenseCallback.onAddSharedExpense(groupExpense)
    }

    ExpenseView(
        groupExpense = groupExpense,
        expenseViewCallback = expenseViewCallback
    )
}

