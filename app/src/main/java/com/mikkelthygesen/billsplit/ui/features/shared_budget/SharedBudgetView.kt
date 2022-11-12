package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.Person
import com.mikkelthygesen.billsplit.paddingOnlyBottom

@Composable
fun SharedBudgetView(
    sharedExpenses: List<SharedExpense>,
    onSharedExpenseClicked: (SharedExpense) -> Unit,
    onFabClicked: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp, 16.dp)
    ) {
        LazyColumn {
            items(count = sharedExpenses.size, key = { sharedExpenses[it].id }) { index ->
                val sharedExpense = sharedExpenses[index]
                SharedExpenseListItem(sharedExpense = sharedExpense) {
                    onSharedExpenseClicked(sharedExpense)
                }
            }
        }
    }
}


@Composable
fun SharedExpenseListItem(sharedExpense: SharedExpense, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .paddingOnlyBottom(12.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Gray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 8.dp)
        ) {
            Text(text = sharedExpense.description)
            Text(text = "Shared=$${sharedExpense.sharedExpense.owed}")
            Text(text = "Total=$${sharedExpense.getTotal()}")
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onClick, Modifier.align(Alignment.BottomEnd)
                ) {
                    Text(text = "Open")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSharedExpenseListItem() {
    SharedBudgetView(sharedExpenses = listOf(
        SharedExpense(
            "ID 0", "Hello", Person("Shared", 0F, false), listOf(
                Person("id..", 10F, true),
                Person("id..", 10F, true),
                Person("id..", 10F, true),
            )
        ),
        SharedExpense(
            "ID 1", "Hello", Person("Shared", 0F, false), listOf(
                Person("id..", 10F, true),
                Person("id..", 10F, true),
                Person("id..", 10F, true),
            )
        ),
        SharedExpense(
            "ID 2", "Hello", Person("Shared", 0F, false), listOf(
                Person("id..", 10F, true),
                Person("id..", 10F, true),
                Person("id..", 10F, true),
            )
        ),
    ), onSharedExpenseClicked = {}, onFabClicked = {})
}