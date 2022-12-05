package com.mikkelthygesen.billsplit.ui.features.group.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.*
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.IndividualExpense
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.widgets.CircularImageView
import com.mikkelthygesen.billsplit.ui.widgets.HorizontalDivider

@Composable
fun ChangesListView(
    groupExpensesChanged: GroupExpensesChanged,
    isLastMessage: Boolean,
    onClickGoToExpense: (String) -> Unit
) {
    val original = groupExpensesChanged.groupExpenseOriginal
    val updated = groupExpensesChanged.groupExpenseEdited
    var expanded by remember {
        mutableStateOf(isLastMessage)
    }
    val wasTotalChanged = original.total != updated.total
    val wasPayerChanged = original.payeeState != updated.payeeState
    val wasDescriptionChanged =
        original.descriptionState != updated.descriptionState
    val wasParticipantsChanged = original.getParticipants() != updated.getParticipants()

    ExpandableView(
        modifier = Modifier
            .wrapContentHeight()
            .clickable { expanded = !expanded },
        expanded = expanded,
        iconResId = R.drawable.ic_baseline_search_24,
        onIconClick = { onClickGoToExpense(groupExpensesChanged.groupExpenseOriginal.id) },
        title = {
            Text(text = "${groupExpensesChanged.createdBy.nameState} made changes to an expense")
        }
    ) {
        if (expanded) {
            Column {
                if (wasTotalChanged) {
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SmallRoundImage(R.drawable.ic_money)
                        Text(text = "$${groupExpensesChanged.groupExpenseOriginal.total.fmt2dec()} ▶ $${groupExpensesChanged.groupExpenseEdited.total.fmt2dec()}")
                    }
                }
                if (wasPayerChanged) {
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SmallRoundImage(R.drawable.ic_money)
                        Text(text = "${original.payeeState.nameState} ▶ ${updated.payeeState.nameState}")
                    }
                }
                if (wasDescriptionChanged) {
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SmallRoundImage(R.drawable.ic_baseline_edit_24)
                        Text(
                            text = "\"${updated.descriptionState}\"",
                            style = TextStyle(fontStyle = FontStyle.Italic)
                        )
                    }
                }
                if (wasParticipantsChanged) {
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SmallRoundImage(R.drawable.ic_baseline_groups_24)
                        ParticipantsView(list = original.getParticipants())
                        Text(text = " ▶ ")
                        ParticipantsView(list = updated.getParticipants())
                    }
                }
                if (wasDescriptionChanged) {
                    HorizontalDivider()
                    Column {
                        original.individualExpenses.mapIndexed { index, originalExpense ->
                            val updatedExpense = updated.individualExpenses[index]
                            if (originalExpense.expenseState != updatedExpense.expenseState) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularImageView(
                                        Modifier
                                            .height(20.dp)
                                            .padding(horizontal = 16.dp),
                                        imageResId = originalExpense.person.pfpResId
                                    )
                                    Text(text = "$${originalExpense.expenseState.fmt2dec()} ▶ $${updatedExpense.expenseState.fmt2dec()}")
                                }
                                if (index != original.individualExpenses.lastIndex)
                                    HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallRoundImage(resId: Int) {
    CircularImageView(
        Modifier
            .height(20.dp)
            .padding(horizontal = 16.dp),
        imageResId = resId
    )
}

@Preview
@Composable
private fun Preview() {
    val createdBy = samplePeopleShera.first()
    val people = (0..2).map { Person("Person$it", "Person$it", R.drawable.shera_pfp) }

    // original
    val payerOriginal = people.first()
    val individualExpensesOriginal = people.mapIndexed { index, person ->
        IndividualExpense(person, 200F * index, true)
    }
    val expenseOriginal = GroupExpense(
        id = "1",
        createdBy = createdBy,
        sharedExpense = 400F,
        description = "Hello",
        payee = payerOriginal,
        individualExpenses = individualExpensesOriginal
    )
    val payerEdited = people[1]
    val individualExpensesEdited = people.mapIndexed { index, person ->
        IndividualExpense(person, 230F * index, index == 2)
    }
    val expenseEdited = GroupExpense(
        id = "1",
        createdBy = createdBy,
        sharedExpense = 2000F,
        description = "Hello World",
        payee = payerEdited,
        individualExpenses = individualExpensesEdited
    )
    val groupExpensesChanged = GroupExpensesChanged(
        id = "",
        groupExpenseOriginal = expenseOriginal,
        groupExpenseEdited = expenseEdited,
        createdBy = expenseOriginal.createdBy,
    )
    Box(Modifier.height(300.dp)) {
        ChangesListView(
            groupExpensesChanged = groupExpensesChanged,
            isLastMessage = true,
            onClickGoToExpense = {})
    }
}


