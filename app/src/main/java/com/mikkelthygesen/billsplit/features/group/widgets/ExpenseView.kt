package com.mikkelthygesen.billsplit.features.group.widgets

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.*
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.IndividualExpense
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.widgets.ExpenseTextField
import com.mikkelthygesen.billsplit.ui.widgets.ProfilePicture

data class PersonViewFlags(
    val enableEditName: Boolean,
    val enableParticipationToggle: Boolean,
    val enableRemoval: Boolean
) {
    companion object {
        fun allDisabled() = PersonViewFlags(
            enableEditName = false, enableParticipationToggle = false, enableRemoval = false
        )

        fun participant() = PersonViewFlags(
            enableEditName = false, enableParticipationToggle = true, enableRemoval = false
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonView(
    modifier: Modifier,
    expenseHolder: IndividualExpense,
    groupExpense: GroupExpense,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: (IndividualExpense) -> Unit,
    onScrollToPosition: suspend () -> Unit,
    flags: PersonViewFlags,
    sharedExpense: Float = 0F,
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var isEditing by remember {
        mutableStateOf(false)
    }
    val isParticipant = expenseHolder.isParticipantState
    val totalExpense =
        if (isParticipant)
            expenseHolder.expenseState + sharedExpense else expenseHolder.expenseState

    Row(
        modifier = modifier
            .padding(12.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .weight(1f)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            ProfileView(groupExpense = groupExpense, expenseHolder = expenseHolder)
            if (flags.enableRemoval)
                IconButton(
                    onClick = {
                        onRemoveClicked(expenseHolder)
                    }, modifier = Modifier.width(64.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                        contentDescription = ""
                    )
                }
        }

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(4f)
                .fillMaxWidth()
        ) {
            if (showDialog) ChangeNameDialog(
                textFieldValue = expenseHolder.person.nameState,
                onConfirm = {
                    expenseHolder.person.nameState = it
                    showDialog = false
                },
                onDismiss = { showDialog = false })
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Top
            ) {
                val nameText = if (groupExpense.payeeState == expenseHolder.person) "" +
                        "${expenseHolder.person.nameState} is paying" else expenseHolder.person.nameState
                ClickableText(
                    text = AnnotatedString(nameText),
                    onClick = {
                        if (flags.enableEditName) showDialog = true
                    },
                    style = TextStyle(fontSize = 15.sp, color = MaterialTheme.colors.onBackground),
                )
                if (flags.enableParticipationToggle)
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement.provides(false)) {
                        Checkbox(
                            modifier = Modifier
                                .defaultMinSize(1.dp)
                                .padding(0.dp),
                            checked = expenseHolder.isParticipantState,
                            onCheckedChange = {
                                expenseHolder.isParticipantState = it
                            })
                    }
            }
            Row(
                verticalAlignment = CenterVertically
            ) {
                Crossfade(
                    modifier = Modifier.weight(5f),
                    targetState = isEditing
                ) {
                    if (it) ExpenseTextField(
                        expenseHolder = expenseHolder,
                        onChangeListener = onChangeListener,
                        onScrollPosition = onScrollToPosition
                    ) {
                        isEditing = false
                    }
                    else ExpenseDisplay(expenseHolder, sharedExpense) {
                        isEditing = true
                    }
                }
                Box(Modifier.weight(1f)) {}
                Text(
                    text = "$${totalExpense.fmt2dec()}",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
        }
    }
}

@Composable
private fun ProfileView(groupExpense: GroupExpense, expenseHolder: IndividualExpense) {
    val isPayee = groupExpense.payeeState == expenseHolder.person
    Box {
        if (!expenseHolder.isShared())
            ProfilePicture(
                modifier = Modifier
                    .width(64.dp)
                    .aspectRatio(1F)
                    .clip(CircleShape)
                    .clickable {
                        groupExpense.payeeState = expenseHolder.person
                    },
                person = expenseHolder.person
            )
        else
            Image(
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
                    .background(Color.Cyan, CircleShape)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.ic_baseline_groups_24),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        if (isPayee)
            Icon(
                painter = painterResource(id = R.drawable.ic_money),
                tint = Color.White,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = CircleShape
                    )
                    .align(Center),
                contentDescription = "",
            )
    }
}

@Composable
private fun ExpenseDisplay(
    expenseHolder: IndividualExpense,
    sharedExpense: Float,
    onClick: () -> Unit,
) {
    val isParticipant = expenseHolder.isParticipantState
    val isExpenseFloat = expenseHolder.expenseState.rem(1) != 0.0f
    val expenseString = if (isExpenseFloat) String.format("%,.2f", expenseHolder.expenseState)
    else expenseHolder.expenseState.toInt()

    val isSharedExpenseFloat = sharedExpense.rem(1) != 0.0f
    val sharedExpenseString = if (isSharedExpenseFloat) String.format("%,.2f", sharedExpense)
    else sharedExpense.toInt()

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 23.sp, color = MaterialTheme.colors.onBackground)) {
            append(text = "$$expenseString")
        }
        if (isParticipant && sharedExpense > 0f)
            withStyle(SpanStyle(fontSize = 20.sp, color = Color.Gray)) {
                append(" + $$sharedExpenseString")
            }
    }

    ClickableText(
        modifier = Modifier.fillMaxWidth(),
        text = annotatedString,
        onClick = { onClick() })
}

@Composable
private fun ChangeNameDialog(
    textFieldValue: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var changeNameTextFieldValue by remember {
        mutableStateOf(textFieldValue)
    }
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text(text = "Change participant's name") },
        text = {
            TextField(value = changeNameTextFieldValue,
                onValueChange = { changeNameTextFieldValue = it },
                isError = changeNameTextFieldValue.isBlank(),
                placeholder = {
                    Text(text = "Enter a name")
                },
                trailingIcon = {
                    IconButton(onClick = { changeNameTextFieldValue = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_cancel_24),
                            contentDescription = "Clear text"
                        )
                    }
                })
        },
        confirmButton = {
            Button(onClick = {
                if (changeNameTextFieldValue.isNotBlank())
                    onConfirm(changeNameTextFieldValue)
            }) {
                Text(text = "Apply")
            }
        })
}

@Preview(showBackground = true)
@Composable
private fun PreviewPersonViewWithRemove() {
    val flags = PersonViewFlags(
        enableEditName = true, enableParticipationToggle = true, enableRemoval = true
    )
    val person = Person("0", "Mikkel")
    val individualExpense = IndividualExpense(person, 1000f, true)
    val sharedExpenseHolder = 1000f
    val groupExpense = GroupExpense(
        "000",
        person,
        "",
        person,
        sharedExpenseHolder,
        listOf(individualExpense)
    )
    PersonView(
        modifier = Modifier,
        expenseHolder = IndividualExpense(Person("id1", "Person 1"), 100F),
        groupExpense = GroupExpense(
            "",
            person,
            "",
            person,
            sharedExpenseHolder,
            groupExpense.individualExpenses
        ),
        onChangeListener = {},
        onRemoveClicked = {},
        flags = flags,
        sharedExpense = 100F,
        onScrollToPosition = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewPersonViewWithoutToggle() {
    val flags = PersonViewFlags(
        enableEditName = true, enableParticipationToggle = false, enableRemoval = false
    )
    val person = Person("0", "Mikkel")
    val individualExpense = IndividualExpense(person, 1000f, true)
    val sharedExpenseHolder = 1000f
    val groupExpense = GroupExpense(
        "000",
        person,
        "",
        person,
        sharedExpenseHolder,
        listOf(individualExpense)
    )
    PersonView(
        modifier = Modifier,
        expenseHolder = IndividualExpense(Person("id1", "Person 1"), 100F),
        groupExpense = GroupExpense(
            "",
            person,
            "",
            person,
            sharedExpenseHolder,
            groupExpense.individualExpenses
        ),
        onChangeListener = {},
        onRemoveClicked = {},
        flags = flags,
        sharedExpense = 100F,
        onScrollToPosition = {}
    )
}

