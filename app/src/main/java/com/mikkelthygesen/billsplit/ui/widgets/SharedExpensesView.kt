package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.Person

@Composable
fun SharedExpensesView(
    modifier: Modifier,
    onScrollToPosition: suspend () -> Unit,
    groupExpense: GroupExpense,
    onChangeListener: (Float) -> Unit
) {
    PersonView(
        modifier = modifier,
        expenseHolder = groupExpense.sharedExpense,
        groupExpense = groupExpense,
        onChangeListener = onChangeListener,
        onRemoveClicked = {},
        flags = PersonViewFlags.allDisabled(),
        onScrollToPosition = onScrollToPosition
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSharedExpenseView() {
    val person = Person("", "Mikkel")
    val individualExpenseHolder = ExpenseHolder.IndividualExpenseHolder(person, 1000f, true)
    val sharedExpenseHolder = ExpenseHolder.SharedExpenseHolder(1000f)
    val groupExpense = GroupExpense(
        "000",
        "",
        person,
        sharedExpenseHolder,
        listOf(individualExpenseHolder)
    )
    SharedExpensesView(
        modifier = Modifier,
        onScrollToPosition = {},
        groupExpense = groupExpense,
        onChangeListener = {})
}