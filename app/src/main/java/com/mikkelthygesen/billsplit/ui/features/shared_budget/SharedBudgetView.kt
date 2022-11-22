package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.paddingBottom

@Composable
fun SharedBudgetView(
    viewModel: SharedBudgetViewModel = viewModel()
) {
    val flow = viewModel.shareableStateFlow().collectAsState(initial = emptyList())
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        val shareablesState = flow.value.sortedBy { it.timeStamp }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            LazyColumn(
                Modifier.align(Alignment.BottomStart)
            ) {
                items(
                    count = shareablesState.size,
                    key = { shareablesState[it].timeStamp }) { index ->
                    when (val shareable = shareablesState[index]) {
                        is GroupExpense -> SharedExpenseListItem(groupExpense = shareable)
                        is Payment -> PaymentListView(payment = shareable)
                        is GroupExpensesChanged -> ChangesListView(groupExpensesChanged = shareable)
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentListView(payment: Payment) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .paddingBottom(12.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 8.dp)
        ) {
            Text(text = "${payment.payee.nameState} paid $${payment.amount} to ${payment.paidTo.nameState}")
        }
    }
}

@Composable
private fun ChangesListView(groupExpensesChanged: GroupExpensesChanged) {
    val original = groupExpensesChanged.groupExpenseOriginal
    val updated = groupExpensesChanged.groupExpenseEdited
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .paddingBottom(12.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 8.dp)
        ) {
            Text(text = "${groupExpensesChanged.editor.nameState} mades changes to an expense")
            Text(text = "Changes made:")
            if (original.getTotal() != updated.getTotal())
                Text(text = "Total: $${groupExpensesChanged.groupExpenseOriginal.getTotal()} -> $${groupExpensesChanged.groupExpenseEdited.getTotal()}")
            if (original.payeeState != updated.payeeState)
                Text(text = "Updated payer: ${original.payeeState.nameState} -> ${updated.payeeState.nameState}")
            if(original.descriptionState != updated.descriptionState)
                Text(text = "Updated description: ${updated.descriptionState}")
        }
    }
}

@Composable
private fun SharedExpenseListItem(
    viewModel: SharedBudgetViewModel = viewModel(),
    groupExpense: GroupExpense
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .paddingBottom(12.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 8.dp)
        ) {
            Text(text = groupExpense.descriptionState)
            Text(text = "Shared=$${groupExpense.sharedExpense.expenseState}")
            Text(text = "Total=$${groupExpense.getTotal()}")
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        viewModel.editSharedExpense(groupExpense)
                    }, Modifier.align(Alignment.BottomEnd),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
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
    SharedBudgetView()
}