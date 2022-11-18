package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.ExpenseHolder

@Composable
fun ParticipantView(
    person: ExpenseHolder.IndividualExpenseHolder,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: (ExpenseHolder.IndividualExpenseHolder) -> Unit,
    sharedOwed: Float
) {
    val totalOwed = if (person.isParticipant)
        person.expense + sharedOwed else person.expense
    PersonView(
        expenseHolder = person,
        onChangeListener = onChangeListener,
        onRemoveClicked = onRemoveClicked,
        owed = "$totalOwed",
        flags = PersonViewFlags.participant()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewParticipantView() {
    val person = ExpenseHolder.IndividualExpenseHolder(Person("id1","Person 1"), 1000F)
    ParticipantView(
        person = person,
        onChangeListener = {},
        onRemoveClicked = {},
        sharedOwed = 100F
    )
}