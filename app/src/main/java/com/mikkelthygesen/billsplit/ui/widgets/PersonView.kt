package com.mikkelthygesen.billsplit.ui.widgets

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.paddingEnd

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

@Composable
fun PersonView(
    modifier: Modifier,
    expenseHolder: ExpenseHolder,
    groupExpense: GroupExpense,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: (ExpenseHolder.IndividualExpenseHolder) -> Unit,
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
    val isParticipant =
        expenseHolder is ExpenseHolder.IndividualExpenseHolder && expenseHolder.isParticipantState
    val totalExpense =
        if (isParticipant)
            expenseHolder.expenseState + sharedExpense else expenseHolder.expenseState

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp), Arrangement.Center

    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .paddingEnd(8.dp)
                .fillMaxWidth(),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            ProfilePicture(groupExpense = groupExpense, expenseHolder = expenseHolder)
            if (flags.enableRemoval && expenseHolder is ExpenseHolder.IndividualExpenseHolder)
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
                .weight(4f)
                .fillMaxWidth()
        ) {
            if (showDialog) ChangeNameDialog(textFieldValue = expenseHolder.nameState, onConfirm = {
                expenseHolder.nameState = it
                showDialog = false
            }, onDismiss = { showDialog = false })
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                ClickableText(
                    text = AnnotatedString(expenseHolder.nameState),
                    onClick = {
                        if (flags.enableEditName) showDialog = true
                    },
                    style = TextStyle(fontSize = 30.sp, color = MaterialTheme.colors.onBackground),
                )
                if (flags.enableParticipationToggle &&
                    expenseHolder is ExpenseHolder.IndividualExpenseHolder
                ) Checkbox(
                    checked = expenseHolder.isParticipantState,
                    onCheckedChange = {
                        expenseHolder.isParticipantState = it
                    })
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
                    text = "$${String.format("%,.2f", totalExpense)}",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
        }
    }
}

@Composable
private fun ProfilePicture(groupExpense: GroupExpense, expenseHolder: ExpenseHolder) {
    val isPayee =
        expenseHolder is ExpenseHolder.IndividualExpenseHolder && groupExpense.payeeState == expenseHolder
    Box {
        if (expenseHolder is ExpenseHolder.IndividualExpenseHolder)
            Image(
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
                    .clip(CircleShape)
                    .blur(30.dp)
                    .clickable {
                        groupExpense.payeeState = expenseHolder
                    },
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Person profile picture, click to mark as payee",
                contentScale = ContentScale.Crop
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
                painter = painterResource(id = R.drawable.ic_check),
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
    expenseHolder: ExpenseHolder,
    sharedExpense: Float,
    onClick: () -> Unit,
) {
    val isParticipant =
        expenseHolder is ExpenseHolder.IndividualExpenseHolder && expenseHolder.isParticipantState

    val isExpenseFloat = expenseHolder.expenseState.rem(1) != 0.0f
    val expenseString = if (isExpenseFloat) String.format("%,.2f", expenseHolder.expenseState)
    else expenseHolder.expenseState.toInt()

    val isSharedExpenseFloat = sharedExpense.rem(1) != 0.0f
    val sharedExpenseString = if (isSharedExpenseFloat) String.format("%,.2f", sharedExpense)
    else sharedExpense.toInt()

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 20.sp, color = MaterialTheme.colors.onBackground)) {
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
                if (changeNameTextFieldValue.isNotBlank()) onConfirm(changeNameTextFieldValue)
            }) {
                Text(text = "Apply")
            }
        })
}

@Preview(showBackground = true)
@Composable
fun PreviewPersonView() {
    val flags = PersonViewFlags(
        enableEditName = true, enableParticipationToggle = true, enableRemoval = true
    )
    val person = Person("0", "Mikkel")
    val individualExpenseHolder = ExpenseHolder.IndividualExpenseHolder(person, 1000f, true)
    val sharedExpenseHolder = ExpenseHolder.SharedExpenseHolder(1000f)
    val groupExpense = GroupExpense(
        "000",
        "",
        individualExpenseHolder,
        sharedExpenseHolder,
        listOf(individualExpenseHolder)
    )
    PersonView(
        modifier = Modifier,
        expenseHolder = ExpenseHolder.IndividualExpenseHolder(Person("id1", "Person 1"), 100F),
        groupExpense = GroupExpense(
            "",
            "",
            individualExpenseHolder,
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