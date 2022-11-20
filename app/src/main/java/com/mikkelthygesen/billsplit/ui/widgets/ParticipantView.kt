package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense

@Composable
fun ParticipantView(
    modifier: Modifier,
    expenseHolder: ExpenseHolder.IndividualExpenseHolder,
    groupExpense: GroupExpense,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: (ExpenseHolder.IndividualExpenseHolder) -> Unit,
    onScrollToPosition: suspend () -> Unit,
    sharedExpense: Float
) {
    PersonView(
        modifier = modifier,
        expenseHolder = expenseHolder,
        groupExpense = groupExpense,
        onChangeListener = onChangeListener,
        onRemoveClicked = onRemoveClicked,
        sharedExpense = sharedExpense,
        flags = PersonViewFlags.participant(),
        onScrollToPosition = onScrollToPosition
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewParticipantView() {
    val person = Person("id1", "Person 1")
    val individualExpenseHolder = ExpenseHolder.IndividualExpenseHolder(person, 1000f, true)
    val sharedExpenseHolder = ExpenseHolder.SharedExpenseHolder(1000f)
    val groupExpense = GroupExpense(
        "000",
        "",
        individualExpenseHolder,
        sharedExpenseHolder,
        listOf(individualExpenseHolder)
    )
    ParticipantView(
        modifier = Modifier,
        expenseHolder = individualExpenseHolder,
        groupExpense = groupExpense,
        onChangeListener = {},
        onRemoveClicked = {},
        onScrollToPosition = {},
        sharedExpense = 100F,
    )
}