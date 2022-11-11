package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.ui.widgets.FabView


@Composable
fun SharedBudgetView(
    sharedExpenses: List<SharedExpense>,
    onSharedExpenseClicked: (SharedExpense) -> Unit,
    onFabClicked: () -> Unit
) {
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FabView(onFabClicked) }
    ) {
        Column {
            LazyColumn {
                items(
                    count = sharedExpenses.size,
                    key = { sharedExpenses[it].id }) { index ->
                    val sharedExpense = sharedExpenses[index]
                    Column(modifier = Modifier.padding(20.dp, 10.dp)) {
                        Text(text = "id: ${sharedExpense.id.subSequence(0, 5)}")
                        Button(onClick = {
                            onSharedExpenseClicked(sharedExpense)
                        }) {
                            Text(text = "${sharedExpense.getTotal()}")
                        }
                    }
                }
            }
        }
    }
}