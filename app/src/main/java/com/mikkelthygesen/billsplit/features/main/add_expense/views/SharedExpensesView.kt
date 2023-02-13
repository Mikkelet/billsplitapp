package com.mikkelthygesen.billsplit.features.main.add_expense.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.IndividualExpense
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.main.group.widgets.PersonView
import com.mikkelthygesen.billsplit.features.main.group.widgets.PersonViewFlags

@Composable
fun SharedExpensesView(
    modifier: Modifier,
    onScrollToPosition: suspend () -> Unit,
    groupExpense: GroupExpense,
    onChangeListener: (Float) -> Unit
) {
    PersonView(
        modifier = modifier,
        expenseHolder = IndividualExpense.getSharedExpenseHolder(groupExpense.sharedExpenseState),
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
    val individualExpense = IndividualExpense(person, 1000f, true)
    val sharedExpenseHolder = 1000f
    val groupExpense = GroupExpense(
        "000",
        person,
        "",
        person,
        sharedExpenseHolder,
        listOf(individualExpense)
    )
    SharedExpensesView(
        modifier = Modifier,
        onScrollToPosition = {},
        groupExpense = groupExpense,
        onChangeListener = {})
}