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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.models.interfaces.IShareable
import com.mikkelthygesen.billsplit.paddingBottom
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

@Composable
fun SharedBudgetView(
    viewModel: SharedBudgetViewModel = viewModel()
) {
    val flow = viewModel.shareableStateFlow().collectAsState(initial = emptyList())
    Column(
        modifier = Modifier.padding(16.dp, 16.dp)
    ) {
        val shareablesState = flow.value.sortedBy { it.timeStamp }
        LazyColumn {
            items(count = shareablesState.size, key = { shareablesState[it].timeStamp }) { index ->
                when (val shareable = shareablesState[index]) {
                    is GroupExpense -> SharedExpenseListItem(groupExpense = shareable)
                    is Payment -> PaymentListView(payment = shareable)
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