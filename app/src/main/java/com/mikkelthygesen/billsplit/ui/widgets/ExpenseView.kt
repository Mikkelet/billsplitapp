package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.Person
import kotlin.math.roundToInt

interface ExpenseViewCallback {
    fun onSharedExpenseUpdate(owed: Float)
    fun onParticipantExpenseUpdate(
        individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder,
        owed: Float
    )

    fun onRemovePerson(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder)
}

@Composable
fun ExpenseView(
    expenseViewCallback: ExpenseViewCallback,
    groupExpense: GroupExpense
) {
    val sharedExpenses = groupExpense.sharedExpense
    val expenseHolders = groupExpense.individualExpenses

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(
            Modifier.verticalScroll(scrollState),
        ) {
            SharedExpensesView(
                groupExpense = groupExpense,
                onChangeListener = { sharedExpenses.expense = it }
            )
            groupExpense.individualExpenses.map { individualExpenseHolder ->
                val numOfParticipants = expenseHolders.count { it.isParticipant }
                var positionInRoot by remember {
                    mutableStateOf(0f)
                }
                Box(modifier = Modifier
                    .onGloballyPositioned {
                        positionInRoot = it.positionInParent().y
                    }
                ) {
                    ParticipantView(
                        expenseHolder = individualExpenseHolder,
                        groupExpense = groupExpense,
                        sharedOwed = sharedExpenses.expense / numOfParticipants,
                        onChangeListener = { individualExpenseHolder.expense = it },
                        onRemoveClicked = expenseViewCallback::onRemovePerson,
                        onScrollToPosition = {
                            println("qqq Scroll to position=$positionInRoot")
                            scrollState.animateScrollTo(positionInRoot.roundToInt())
                        }
                    )
                }
            }
            Box(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun DescriptionTextField(
    initialValue: String,
    onChange: (String) -> Unit
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = initialValue))
    }
    val focusRequester = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = textFieldValue,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = ""
            )
        },
        onValueChange = {
            textFieldValue = it
            onChange(it.text)
        },
        singleLine = true,
        placeholder = { Text(text = "Enter a description") },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.surface,
            textColor = MaterialTheme.colors.onSurface
        ),
        keyboardActions = KeyboardActions {
            focusRequester.clearFocus()
        },
    )
}

@Preview
@Composable
fun PreviewExpenseView() {
    val callback = object : ExpenseViewCallback {
        override fun onSharedExpenseUpdate(owed: Float) = Unit

        override fun onParticipantExpenseUpdate(
            individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder,
            owed: Float
        ) = Unit

        override fun onRemovePerson(individualExpenseHolder: ExpenseHolder.IndividualExpenseHolder) =
            Unit
    }

    val shared = ExpenseHolder.SharedExpenseHolder(100F)
    val participants =
        (0..3).map {
            ExpenseHolder.IndividualExpenseHolder(
                Person("id$it", "Person $it"),
                1 * 100F
            )
        }
    val groupExpense = GroupExpense(
        id = "000",
        "",
        payee = participants[1],
        sharedExpense = shared,
        individualExpenses = participants
    )
    ExpenseView(
        expenseViewCallback = callback,
        groupExpense = groupExpense
    )
}