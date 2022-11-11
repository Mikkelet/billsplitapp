package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.R
import kotlinx.coroutines.flow.StateFlow

interface ExpenseViewCallback {
    fun onSharedExpenseUpdate(owed: Float)
    fun onParticipantExpenseUpdate(person: Person, owed: Float)
    fun onRemovePerson(person: Person)
    fun onFabClick()
}

@Composable
fun ExpenseView(
    expenseViewCallback: ExpenseViewCallback,
    participants: List<Person>,
    sharedExpenses: Person,
    showFab: Boolean = false,
    canEditName: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Scaffold(
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                if (showFab)
                    FloatingActionButton(
                        modifier = Modifier.padding(16.dp),
                        onClick = expenseViewCallback::onFabClick
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_plus),
                            contentDescription = "Add participant"
                        )
                    }
            }
        ) {
            LazyColumn {
                items(
                    count = participants.size,
                    key = { participants[it].name }
                ) { index ->
                    val participant = participants[index]
                    if (index == 0) // Shared is also first participant
                        SharedExpensesView(
                            shared = participant,
                            onChangeListener = { sharedExpenses.owed = it }
                        )
                    else ParticipantView(
                        person = participant,
                        sharedOwed = sharedExpenses.owed / participants.size,
                        onChangeListener = { participant.owed = it },
                        onRemoveClicked = expenseViewCallback::onRemovePerson,
                        canEditName = canEditName
                    )
                }
            }
        }
    }
}