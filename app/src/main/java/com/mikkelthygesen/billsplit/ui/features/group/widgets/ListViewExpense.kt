package com.mikkelthygesen.billsplit.ui.features.group.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.fmt2dec
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel

@Composable
fun ListViewExpense(
    viewModel: GroupViewModel = viewModel(),
    groupExpense: GroupExpense,
    isFocused: Boolean,
    isLastMessage: Boolean
) {
    _ListViewExpense(
        groupExpense = groupExpense,
        isFocused = isFocused,
        isLastMessage = isLastMessage,
        onActionClicked = viewModel::editSharedExpense
    )
}

@SuppressLint("ComposableNaming")
@Composable
private fun _ListViewExpense(
    groupExpense: GroupExpense,
    onActionClicked: (GroupExpense) -> Unit,
    isFocused: Boolean,
    isLastMessage: Boolean
) {
    var expanded by remember {
        mutableStateOf(isLastMessage)
    }
    ExpandableView(
        modifier = Modifier.let {
            if (isFocused)
                it
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.secondary)
                    .padding(2.dp)
                    .clickable { expanded = !expanded }
            else it.clickable { expanded = !expanded }
        },
        expanded = expanded,
        iconResId = R.drawable.ic_baseline_edit_24,
        onIconClick = { onActionClicked(groupExpense) }
    ) {
        if (groupExpense.descriptionState.isNotBlank())
            Text(
                text = "\"${groupExpense.descriptionState}\"",
                style = TextStyle(fontSize = 18.sp, fontStyle = FontStyle.Italic)
            )
        else Text(text = "${groupExpense.createdBy.nameState} added a new expense!")
        Box(modifier = Modifier.height(8.dp))
        if (!expanded)
            Text(
                text = "$${groupExpense.total.fmt2dec()}",
                style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 20.sp)
            )
        else {
            Text(text = "$${groupExpense.total.fmt2dec()} was paid by ${groupExpense.payeeState.nameState}")
            Box(modifier = Modifier.height(8.dp))
            Row {
                Column {
                    if (groupExpense.sharedExpenseState > 0F)
                        Text(text = "Shared")
                    groupExpense.individualExpenses.map {
                        if (it.expenseState > 0F) {
                            Text(text = it.person.nameState)
                        }
                    }
                }
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    if (groupExpense.sharedExpenseState > 0f)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "$${groupExpense.sharedExpenseState.fmt2dec()} / ")
                            val participants =
                                groupExpense.individualExpenses
                                    .filter { it.isParticipantState }
                                    .map { it.person }
                            ParticipantsView(list = participants)
                        }
                    groupExpense.individualExpenses.map {
                        if (it.expenseState > 0F) {
                            Text(text = "$${it.expenseState.fmt2dec()}")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val groupExpense = sampleSharedExpenses.first()
    Box(modifier = Modifier.height(200.dp)) {
        ListViewExpense(groupExpense = groupExpense, isFocused = false, isLastMessage = true)
    }
}