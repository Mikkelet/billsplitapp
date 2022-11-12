package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetView

@Composable
fun SharedExpensesView(
    shared: Person,
    onChangeListener: (Float) -> Unit
) {
    PersonView(
        person = shared,
        onChangeListener = onChangeListener,
        onRemoveClicked = {},
        owed = "${shared.owed}",
        flags = PersonViewFlags.allDisabled()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSharedExpenseView() {
    val shared = Person("Shared", 100F)
    SharedExpensesView(shared = shared, onChangeListener = {})
}