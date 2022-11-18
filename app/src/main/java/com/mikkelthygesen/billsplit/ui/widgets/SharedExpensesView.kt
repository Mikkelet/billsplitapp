package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.models.ExpenseHolder

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