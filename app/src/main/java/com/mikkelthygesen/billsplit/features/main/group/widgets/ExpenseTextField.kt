package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.domain.models.IndividualExpense
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.tryCatchDefault
import com.mikkelthygesen.billsplit.tryParseToFloat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTextField(
    expenseHolder: IndividualExpense,
    onChangeListener: (Float) -> Unit,
    onScrollPosition: suspend () -> Unit,
    onConfirm: () -> Unit,
) {
    var textFieldValue by remember {
        val hasDecimals = expenseHolder.expenseState.rem(1) != 0.0f
        val expense =
            if (hasDecimals) expenseHolder.expenseState else expenseHolder.expenseState.toInt()
        val state =
            if (expenseHolder.expenseState == 0F) "" else "$expense"
        mutableStateOf(TextFieldValue(text = state, selection = TextRange(state.length)))
    }
    var inFocus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val isParticipant = expenseHolder.isParticipantState

    TextField(
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged { state ->
                inFocus = state.hasFocus
                coroutineScope.launch {
                    delay(50) // short delay to guarantee the correct focus state
                    if (!inFocus)
                        onConfirm()
                    if (inFocus)
                        onScrollPosition()
                }
            },
        value = textFieldValue,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(fontSize = 20.sp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_money),
                contentDescription = "",
                tint = if (textFieldError(expenseHolder, textFieldValue.text))
                    MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        },
        isError = textFieldError(expenseHolder, textFieldValue.text),
        onValueChange = {
            textFieldValue = it
            val changeValue = when {
                it.text.isNotBlank() -> tryCatchDefault(0F) {
                    textFieldValue.text.toFloat()
                }
                isParticipant -> 0F
                else -> 0F
            }
            onChangeListener(changeValue)
        },
        placeholder = { Text(text = "0") },
        keyboardActions = KeyboardActions {
            if (!textFieldError(expenseHolder, textFieldValue.text))
                onConfirm()
        },
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


private fun textFieldError(expenseHolder: IndividualExpense, textFieldValue: String) =
    if (textFieldValue.isBlank()) false
    else if (tryParseToFloat(expenseHolder, textFieldValue)) {
        textFieldValue.toFloat() < 0F
    } else true

@Preview(showBackground = true)
@Composable
fun PreviewTextField() {
    Box(modifier = Modifier.padding(16.dp)) {
        ExpenseTextField(expenseHolder = IndividualExpense(
            person = Person("ID0", "Mikkel"),
            expense = 1000F,
            isParticipant = true
        ),
            onChangeListener = {},
            onConfirm = {},
            onScrollPosition = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTextFieldError() {
    Box(modifier = Modifier.padding(16.dp)) {
        ExpenseTextField(expenseHolder = IndividualExpense(
            person = Person("ID0", "Mikkel"),
            expense = -1000F,
            isParticipant = true
        ),
            onChangeListener = {},
            onConfirm = {},
            onScrollPosition = {}
        )
    }
}