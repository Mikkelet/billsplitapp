package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.ExpenseHolder

interface ExpenseViewCallback {
    fun onSharedExpenseUpdate(owed: Float)
    fun onParticipantExpenseUpdate(
        individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder,
        owed: Float
    )

    fun onRemovePerson(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder)
    fun onFabClick()
}

@Composable
fun ExpenseView(
    expenseViewCallback: ExpenseViewCallback,
    expenseHolders: List<ExpenseHolder.IndividualExpenseHolder>,
    sharedExpenses: ExpenseHolder.SharedExpenseHolder,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column {
            SharedExpensesView(
                shared = sharedExpenses,
                onChangeListener = { sharedExpenses.expense = it }
            )
            LazyColumn {
                items(
                    count = expenseHolders.size,
                    key = { expenseHolders[it].name }
                ) { index ->
                    val individualExpenseHolder = expenseHolders[index]
                    val numOfParticipants = expenseHolders.count { it.isParticipant }
                    ParticipantView(
                        person = individualExpenseHolder,
                        sharedOwed = sharedExpenses.expense / numOfParticipants,
                        onChangeListener = { individualExpenseHolder.expense = it },
                        onRemoveClicked = expenseViewCallback::onRemovePerson,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewExpenseView() {
    val callback = object : ExpenseViewCallback {
        override fun onSharedExpenseUpdate(owed: Float) = Unit

        override fun onParticipantExpenseUpdate(
            individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder,
            owed: Float
        ) = Unit

        override fun onRemovePerson(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder) =
            Unit

        override fun onFabClick() = Unit
    }

    val shared = ExpenseHolder.SharedExpenseHolder(100F)
    val participants =
        (0..3).map {
            ExpenseHolder.IndividualExpenseHolder(
                Person("id$it", "Person $it"),
                1 * 100F
            )
        }
    ExpenseView(
        expenseViewCallback = callback,
        expenseHolders = participants,
        sharedExpenses = shared
    )
}