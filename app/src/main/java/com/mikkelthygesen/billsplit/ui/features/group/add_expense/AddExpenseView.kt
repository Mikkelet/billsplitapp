package com.mikkelthygesen.billsplit.ui.features.group.add_expense

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.IndividualExpense
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.widgets.ParticipantView
import com.mikkelthygesen.billsplit.ui.widgets.SharedExpensesView
import kotlin.math.roundToInt


private val SCROLL_OFFSET = 200.dp.value


@Composable
fun QuickExpenseView(
    groupExpense: GroupExpense
) {
    ExpenseView(groupExpense = groupExpense)
}

@Composable
fun AddSharedExpense(
    groupExpense: GroupExpense
) {
    ExpenseView(groupExpense = groupExpense)
}


@Composable
fun ExpenseView(
    groupExpense: GroupExpense
) {
    val sharedExpense = groupExpense.sharedExpenseState
    val expenseHolders = groupExpense.individualExpenses

    val scrollState = rememberScrollState()
    var sharedExpensePositionY by remember {
        mutableStateOf(0F)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Header(groupExpense)
            SharedExpensesView(
                modifier = Modifier.onGloballyPositioned {
                    sharedExpensePositionY = it.positionInParent().y - SCROLL_OFFSET
                },
                onScrollToPosition = {
                    scrollState.animateScrollTo(sharedExpensePositionY.roundToInt())
                },
                groupExpense = groupExpense,
                onChangeListener = { groupExpense.sharedExpenseState = it }
            )
            expenseHolders.map { individualExpenseHolder ->
                val numOfParticipants = expenseHolders.count { it.isParticipantState }
                var positionY = 0f
                ParticipantView(
                    modifier = Modifier.onGloballyPositioned {
                        positionY = it.positionInParent().y - SCROLL_OFFSET
                    },
                    expenseHolder = individualExpenseHolder,
                    groupExpense = groupExpense,
                    sharedExpense = sharedExpense / numOfParticipants,
                    onChangeListener = { individualExpenseHolder.expenseState = it },
                    onRemoveClicked = {},
                    onScrollToPosition = {
                        scrollState.animateScrollTo(positionY.roundToInt())
                    }
                )
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
                contentDescription = "",
                tint = MaterialTheme.colors.onBackground
            )
        },
        textStyle = TextStyle(
            color = MaterialTheme.colors.onBackground
        ),
        singleLine = true,
        onValueChange = {
            textFieldValue = it
            onChange(it.text)
        },
        placeholder = { Text(text = "Enter a description") },
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.background.copy(alpha = .7f),
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
    Box(Modifier.padding(bottom = 32.dp)) {
        Image(
            painter = painterResource(id = R.drawable.best_restraunts),
            contentDescription = "",
        )
        DescriptionTextField(
            Modifier.align(Alignment.BottomStart),
            initialValue = groupExpense.descriptionState
        ) {
            groupExpense.descriptionState = it
        }
    }
}

@Preview
@Composable
fun PreviewExpenseView() {
    val shared = 100F
    val participants =
        (0..3).map {
            IndividualExpense(
                Person("id$it", "Person $it"),
                1 * 100F
            )
        }
    val groupExpense = GroupExpense(
        id = "000",
        participants.first().person,
        "",
        payee = participants.first().person,
        sharedExpense = shared,
        individualExpenses = participants
    )
    ExpenseView(groupExpense = groupExpense)
}