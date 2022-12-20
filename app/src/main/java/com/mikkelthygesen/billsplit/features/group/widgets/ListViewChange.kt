package com.mikkelthygesen.billsplit.features.group.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.fmt2dec
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.IndividualExpense
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.ui.widgets.CircularImageView
import com.mikkelthygesen.billsplit.ui.widgets.HorizontalDivider
import com.mikkelthygesen.billsplit.ui.widgets.ProfilePicture

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
            TextStylePrimary(text = "${groupExpensesChanged.createdBy.nameState} made changes to an expense")
        }
    ) {
        if (expanded) {
            Column(
                Modifier.padding(bottom = 12.dp)
            ) {
                if (wasTotalChanged) {
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SmallRoundIcon(R.drawable.ic_money)
                        TextStylePrimary(text = "$${groupExpensesChanged.groupExpenseOriginal.total.fmt2dec()} ▶ $${groupExpensesChanged.groupExpenseEdited.total.fmt2dec()}")
                    }
                }
                if (wasPayerChanged) {
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SmallRoundIcon(R.drawable.ic_money)
                        TextStylePrimary(text = "${original.payeeState.nameState} ▶ ${updated.payeeState.nameState}")
                    }
                }
                if (wasDescriptionChanged) {
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SmallRoundIcon(R.drawable.ic_baseline_edit_24)
                        TextStylePrimary(
                            text = "\"${updated.descriptionState}\"",
                            style = TextStyle(fontStyle = FontStyle.Italic)
                        )
                    }
                }
                if (wasParticipantsChanged) {
                    HorizontalDivider()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SmallRoundIcon(R.drawable.ic_baseline_groups_24)
                        ParticipantsView(list = original.getParticipants())
                        TextStylePrimary(text = " ▶ ")
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
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                    ) {
                                        ProfilePicture(
                                            modifier = Modifier
                                                .size(20.dp),
                                            person = updatedExpense.person
                                        )
                                    }
                                    TextStylePrimary(text = "$${originalExpense.expenseState.fmt2dec()} ▶ $${updatedExpense.expenseState.fmt2dec()}")
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
private fun TextStylePrimary(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = TextStyle()
) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1.merge(style)
            .copy(color = MaterialTheme.colors.onPrimary)
    )
}

@Composable
private fun SmallRoundIcon(resId: Int) {
    Icon(
        modifier = Modifier
            .height(20.dp)
            .padding(horizontal = 16.dp),
        painter = painterResource(id = resId),
        tint = MaterialTheme.colors.onPrimary,
        contentDescription = ""
    )
}

@Preview
@Composable
private fun Preview() {
    val createdBy = samplePeopleShera.first()
    val people = (0..2).map { Person("Person$it", "Person$it") }

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
    Box(Modifier.height(400.dp)) {
        ChangesListView(
            groupExpensesChanged = groupExpensesChanged,
            isLastMessage = true,
            onClickGoToExpense = {})
    }
}


