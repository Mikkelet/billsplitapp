package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.R

interface ExpenseViewCallback {
    fun onSharedExpenseUpdate(owed: Float)
    fun onParticipantExpenseUpdate(person: Person, owed: Float)
    fun onRemovePerson(person: Person)
    fun onFabClick()
}

@Composable
fun ExpenseView(
    expenseViewCallback: ExpenseViewCallback,
    people: List<Person>,
    sharedExpenses: Person,
    showFab: Boolean = false,
    canEditName: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        LazyColumn {
            items(
                count = people.size,
                key = { people[it].name }
            ) { index ->
                val person = people[index]
                val numOfParticipants = people.count { it.isParticipant }
                if (index == 0) // Shared is also first participant
                    SharedExpensesView(
                        shared = person,
                        onChangeListener = { sharedExpenses.owed = it }
                    )
                else ParticipantView(
                    person = person,
                    sharedOwed = sharedExpenses.owed / numOfParticipants,
                    onChangeListener = { person.owed = it },
                    onRemoveClicked = expenseViewCallback::onRemovePerson,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewExpenseView() {
    val callback = object : ExpenseViewCallback {
        override fun onSharedExpenseUpdate(owed: Float) = Unit

        override fun onParticipantExpenseUpdate(person: Person, owed: Float) = Unit

        override fun onRemovePerson(person: Person) = Unit

        override fun onFabClick() = Unit
    }

    val shared = Person("Shared", 100F, isParticipant = false)
    val participants = (0..1).map { Person("Person $it", 1 * 100F) }
    ExpenseView(expenseViewCallback = callback, people = participants, sharedExpenses = shared)

}