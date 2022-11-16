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
import com.mikkelthygesen.billsplit.models.ExpenseHolder

interface ExpenseViewCallback {
    fun onSharedExpenseUpdate(owed: Float)
    fun onParticipantExpenseUpdate(person: Person, owed: Float)
    fun onRemovePerson(person: ExpenseHolder.IndividualExpenseHolder)
    fun onFabClick()
}

@Composable
fun ExpenseView(
    expenseViewCallback: ExpenseViewCallback,
    expenseHolders: List<ExpenseHolder.IndividualExpenseHolder>,
    sharedExpenses: ExpenseHolder.SharedExpenseHolder,
    showFab: Boolean = false,
    canEditName: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        LazyColumn {
            items(
                count = expenseHolders.size,
                key = { expenseHolders[it].name }
            ) { index ->
                val individualExpenseHolder = expenseHolders[index]
                val numOfParticipants = expenseHolders.count { it.isParticipant }
                if (index == 0) // Shared is also first participant
                    SharedExpensesView(
                        shared = sharedExpenses,
                        onChangeListener = { sharedExpenses.expense = it }
                    )
                else ParticipantView(
                    person = individualExpenseHolder,
                    sharedOwed = sharedExpenses.expense / numOfParticipants,
                    onChangeListener = { individualExpenseHolder.expense = it },
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

        override fun onRemovePerson(person: ExpenseHolder.IndividualExpenseHolder) = Unit

        override fun onFabClick() = Unit
    }

    val shared = ExpenseHolder.SharedExpenseHolder(100F)
    val participants = (0..1).map { ExpenseHolder.IndividualExpenseHolder("Person $it", 1 * 100F) }
    ExpenseView(
        expenseViewCallback = callback,
        expenseHolders = participants,
        sharedExpenses = shared
    )

}