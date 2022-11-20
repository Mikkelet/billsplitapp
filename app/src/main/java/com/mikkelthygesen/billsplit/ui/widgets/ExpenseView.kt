package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.mikkelthygesen.billsplit.paddingBottom

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
            Header(groupExpense)
            SharedExpensesView(
                groupExpense = groupExpense,
                onChangeListener = { sharedExpenses.expenseState = it }
            )
            groupExpense.individualExpenses.map { individualExpenseHolder ->
                val numOfParticipants = expenseHolders.count { it.isParticipantState }
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
                        sharedOwed = sharedExpenses.expenseState / numOfParticipants,
                        onChangeListener = { individualExpenseHolder.expenseState = it },
                        onRemoveClicked = expenseViewCallback::onRemovePerson,
                        onScrollToPosition = {
                            // TODO make less janky
                            //scrollState.animateScrollTo(positionInRoot.roundToInt())
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
    modifier: Modifier,
    initialValue: String,
    onChange: (String) -> Unit
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = initialValue))
    }
    val focusRequester = LocalFocusManager.current
    TextField(
        modifier = modifier
            .fillMaxWidth(),
        value = textFieldValue,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = ""
            )
        },
        singleLine = true,
        onValueChange = {
            textFieldValue = it
            onChange(it.text)
        },
        placeholder = { Text(text = "Enter a description") },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            backgroundColor = Color(0xAA000000),
            textColor = Color.White
        ),
        keyboardActions = KeyboardActions {
            focusRequester.clearFocus()
        },
    )
}

@Composable
private fun Header(
    groupExpense: GroupExpense
) {
    Box(Modifier.paddingBottom(32.dp)) {
        Image(
            painter = painterResource(id = R.drawable.best_restraunts),
            contentDescription = "",
        )
       DescriptionTextField(
           Modifier.align(Alignment.BottomStart),
           initialValue = groupExpense.descriptionState){
           groupExpense.descriptionState = it
       }
    }
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