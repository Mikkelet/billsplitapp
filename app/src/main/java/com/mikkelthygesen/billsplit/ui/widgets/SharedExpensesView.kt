package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.ui.features.shared_budget.GroupExpense
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetView

@Composable
fun SharedExpensesView(
    shared: ExpenseHolder.SharedExpenseHolder,
    onChangeListener: (Float) -> Unit
) {
    PersonView(
        expenseHolder = shared,
        onChangeListener = onChangeListener,
        onRemoveClicked = {},
        owed = "${shared.expense}",
        flags = PersonViewFlags.allDisabled()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSharedExpenseView() {
    val shared = ExpenseHolder.SharedExpenseHolder(100F)
    SharedExpensesView(shared = shared, onChangeListener = {})
}