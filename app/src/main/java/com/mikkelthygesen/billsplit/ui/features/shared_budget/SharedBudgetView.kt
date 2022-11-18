package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.paddingBottom
import com.mikkelthygesen.billsplit.sampleSharedExpenses

@Composable
fun SharedBudgetView(
    sharedExpenses: List<GroupExpense>,
    onSharedExpenseClicked: (GroupExpense) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp, 16.dp)
    ) {
        LazyColumn {
            items(count = sharedExpenses.size, key = { sharedExpenses[it].id }) { index ->
                val sharedExpense = sharedExpenses[index]
                SharedExpenseListItem(groupExpense = sharedExpense) {
                    onSharedExpenseClicked(sharedExpense)
                }
            }
        }
    }
}

@Composable
fun SharedExpenseListItem(groupExpense: GroupExpense, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .paddingBottom(12.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFBEEBAF))
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 8.dp)
        ) {
            Text(text = groupExpense.description)
            Text(text = "Shared=$${groupExpense.sharedExpense.expense}")
            Text(text = "Total=$${groupExpense.getTotal()}")
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onClick, Modifier.align(Alignment.BottomEnd),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF92D497)
                    )
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
    SharedBudgetView(
        sharedExpenses = sampleSharedExpenses(),
        onSharedExpenseClicked = {},
    )
}