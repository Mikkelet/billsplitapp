package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.sampleSharedExpenses

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
                        person = shareable.createdBy,
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
                    ProfilePicture(
                        person = shareable.createdBy,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
            }
        }
    }
}

@Composable
private fun ProfilePicture(person: Person, modifier: Modifier) {
    Image(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape),
        painter = painterResource(id = person.pfpResId),
        contentDescription = "Person profile picture for ${person.nameState}",
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
    var expanded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .clickable { expanded = !expanded }
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 12.dp, horizontal = 8.dp)
            .animateContentSize()

    ) {
        Text(text = "${groupExpensesChanged.createdBy.nameState} made changes to an expense")
        if (expanded) {
            if (original.getTotal() != updated.getTotal())
                Text(text = "- Total: $${groupExpensesChanged.groupExpenseOriginal.getTotal()} -> $${groupExpensesChanged.groupExpenseEdited.getTotal()}")
            if (original.payeeState != updated.payeeState)
                Text(text = "- Updated payer: ${original.payeeState.nameState} -> ${updated.payeeState.nameState}")
            if (original.descriptionState != updated.descriptionState)
                Text(text = "- Updated description: ${updated.descriptionState}")
            original.individualExpenses.mapIndexed { index, originalExpense ->
                val updatedExpense = updated.individualExpenses[index]
                if (originalExpense.isParticipantState != updatedExpense.isParticipantState) {
                    if (updatedExpense.isParticipantState)
                        Text(text = "- Updated ${originalExpense.person.nameState} to pay the shared expense")
                    else Text(text = "- Updated ${originalExpense.person.nameState} to not pay the shared expense")
                }
                if (originalExpense.expenseState != updatedExpense.expenseState)
                    Text(text = "- Updated expense for ${originalExpense.person.nameState}: $${originalExpense.expenseState} -> $${updatedExpense.expenseState}")
            }
        }
    }
}

@Composable
private fun SharedExpenseListItem(
    viewModel: SharedBudgetViewModel = viewModel(),
    groupExpense: GroupExpense
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .clickable { expanded = !expanded }
            .animateContentSize()
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            if (groupExpense.descriptionState.isNotBlank())
                Text(text = groupExpense.descriptionState)
            else Text(text = "${groupExpense.createdBy.nameState} added a new expense!")
            Text(text = "$${groupExpense.getTotal()} was paid by ${groupExpense.payeeState.nameState}")
            if (expanded) {
                if (groupExpense.sharedExpenseState > 0F)
                    Row {
                        Column {
                            if (groupExpense.sharedExpenseState > 0F)
                                Text(text = "Shared expenses")
                            groupExpense.individualExpenses.map {
                                if (it.expenseState > 0F) {
                                    Text(text = it.person.nameState)
                                }
                            }
                        }
                        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                            if (groupExpense.sharedExpenseState > 0f)
                                Text(text = "$${groupExpense.sharedExpenseState}")
                            groupExpense.individualExpenses.map {
                                if (it.expenseState > 0F) {
                                    Text(text = "$${it.expenseState}")
                                }
                            }
                        }
                    }
            }
        }
        Button(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            onClick = { viewModel.editSharedExpense(groupExpense) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary
            )
        ) {
            Text(text = "Open", style = TextStyle(color = MaterialTheme.colors.onSecondary))
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewSharedExpenseListItem() {
    val groupExpense = sampleSharedExpenses.first()
    Box(modifier = Modifier.height(100.dp)) {
        SharedExpenseListItem(groupExpense = groupExpense)
    }
}