package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense

@Composable
fun ParticipantView(
    expenseHolder: ExpenseHolder.IndividualExpenseHolder,
    groupExpense: GroupExpense,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: (ExpenseHolder.IndividualExpenseHolder) -> Unit,
    onScrollToPosition: suspend (Float) -> Unit,
    sharedOwed: Float
) {
    PersonView(
        expenseHolder = expenseHolder,
        groupExpense = groupExpense,
        onChangeListener = onChangeListener,
        onRemoveClicked = onRemoveClicked,
        sharedExpense = sharedOwed,
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
        expenseHolder = individualExpenseHolder,
        groupExpense = groupExpense,
        onChangeListener = {},
        onRemoveClicked = {},
        onScrollToPosition = {},
        sharedOwed = 100F,
    )
}