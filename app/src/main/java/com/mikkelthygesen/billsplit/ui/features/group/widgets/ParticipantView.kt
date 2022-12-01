package com.mikkelthygesen.billsplit.ui.features.group.widgets

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.IndividualExpense

@Composable
fun ParticipantView(
    modifier: Modifier,
    expenseHolder: IndividualExpense,
    groupExpense: GroupExpense,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: (IndividualExpense) -> Unit,
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
    val individualExpense = IndividualExpense(person, 1000f, true)
    val sharedExpenseHolder = 1000F
    val groupExpense = GroupExpense(
        "000",
        person,
        "",
        person,
        sharedExpenseHolder,
        listOf(individualExpense)
    )
    ParticipantView(
        modifier = Modifier,
        expenseHolder = individualExpense,
        groupExpense = groupExpense,
        onChangeListener = {},
        onRemoveClicked = {},
        onScrollToPosition = {},
        sharedExpense = 100F,
    )
}