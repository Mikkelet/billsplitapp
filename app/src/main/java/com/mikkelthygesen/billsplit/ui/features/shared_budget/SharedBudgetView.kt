package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment

@Composable
fun SharedBudgetView(
    viewModel: SharedBudgetViewModel = viewModel()
) {
    val flow = viewModel.shareableStateFlow().collectAsState(initial = emptyList())
    val shareablesState = flow.value.sortedBy { it.timeStamp }.reversed()
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        reverseLayout = true
    ) {
        items(
            count = shareablesState.size,
            key = { shareablesState[it].timeStamp }) { index ->
            Row(
                Modifier.padding(vertical = 4.dp)
            ) {
                val shareable = shareablesState[index]
                if (shareable.createdBy != viewModel.getLoggedIn())
                    ProfilePicture(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                Box(
                    modifier = Modifier
                        .weight(6f)
                        .fillMaxWidth()
                ) {
                    when (shareable) {
                        is GroupExpense -> SharedExpenseListItem(groupExpense = shareable)
                        is Payment -> PaymentListView(payment = shareable)
                        is GroupExpensesChanged -> ChangesListView(groupExpensesChanged = shareable)
                    }
                }
                if (shareable.createdBy == viewModel.getLoggedIn())
                    ProfilePicture(modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp))
            }
        }
    }
}

@Composable
private fun ProfilePicture(modifier: Modifier) {
    Image(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .blur(30.dp),
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "Person profile picture for ",
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun PaymentListView(payment: Payment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Text(
            text = "${payment.createdBy.nameState} paid $${payment.amount} to ${payment.paidTo.nameState}"
        )
    }
}

@Composable
private fun ChangesListView(groupExpensesChanged: GroupExpensesChanged) {
    val original = groupExpensesChanged.groupExpenseOriginal
    val updated = groupExpensesChanged.groupExpenseEdited
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Text(text = "${groupExpensesChanged.createdBy.nameState} mades changes to an expense")
        Text(text = "Changes made:")
        if (original.getTotal() != updated.getTotal())
            Text(text = "Total: $${groupExpensesChanged.groupExpenseOriginal.getTotal()} -> $${groupExpensesChanged.groupExpenseEdited.getTotal()}")
        if (original.payeeState != updated.payeeState)
            Text(text = "Updated payer: ${original.payeeState.nameState} -> ${updated.payeeState.nameState}")
        if (original.descriptionState != updated.descriptionState)
            Text(text = "Updated description: ${updated.descriptionState}")
        original.individualExpenses.mapIndexed { index, originalExpense ->
            val updatedExpense = updated.individualExpenses[index]
            if (originalExpense.isParticipantState != updatedExpense.isParticipantState) {
                if (updatedExpense.isParticipantState)
                    Text(text = "Updated ${originalExpense.person.nameState} to pay the shared expense")
                else Text(text = "Updated ${originalExpense.person.nameState} to not pay the shared expense")
            }
            if (originalExpense.expenseState != updatedExpense.expenseState)
                Text(text = "Updated expense for ${originalExpense.person.nameState}: $${originalExpense.expenseState} -> $${updatedExpense.expenseState}")
        }
    }
}

@Composable
private fun SharedExpenseListItem(
    viewModel: SharedBudgetViewModel = viewModel(),
    groupExpense: GroupExpense
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Text(text = groupExpense.descriptionState)
        Text(text = "Shared=$${groupExpense.sharedExpenseState}")
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


@Preview
@Composable
fun PreviewSharedExpenseListItem() {
    SharedBudgetView()
}