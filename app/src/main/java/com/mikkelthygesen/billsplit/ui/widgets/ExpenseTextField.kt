package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.ExpenseHolder
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.tryCatchDefault
import com.mikkelthygesen.billsplit.tryParseToFloat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExpenseTextField(
    expenseHolder: ExpenseHolder,
    onChangeListener: (Float) -> Unit,
    onConfirm: () -> Unit
) {
    var textFieldValue by remember {
        val hasDecimals = expenseHolder.expense.rem(1) != 0.0f
        val expense = if (hasDecimals) expenseHolder.expense else expenseHolder.expense.toInt()
        val state =
            if (expenseHolder.expense == 0F) "" else "$expense"
        mutableStateOf(state)
    }
    var inFocus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val isParticipant =
        expenseHolder is ExpenseHolder.IndividualExpenseHolder && expenseHolder.isParticipant

    OutlinedTextField(
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged { state ->
                inFocus = state.isFocused
                coroutineScope.launch {
                    delay(50)
                    if (!inFocus)
                        onConfirm()
                }
            },
        value = textFieldValue,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_money),
                contentDescription = "",
                tint = if (textFieldError(expenseHolder, textFieldValue))
                    MaterialTheme.colors.error else MaterialTheme.colors.primary
            )
        },
        isError = textFieldError(expenseHolder, textFieldValue),
        onValueChange = {
            textFieldValue = it
            val changeValue = when {
                it.isNotBlank() -> tryCatchDefault(0F) {
                    textFieldValue.toFloat()
                }
                isParticipant -> 0F
                else -> 0F
            }
            onChangeListener(changeValue)
        },
        placeholder = { Text(text = "$0") },
        keyboardActions = KeyboardActions {
            if (!textFieldError(expenseHolder, textFieldValue))
                onConfirm()
        },
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


private fun textFieldError(expenseHolder: ExpenseHolder, textFieldValue: String) =
    if (textFieldValue.isBlank()) false
    else tryParseToFloat(expenseHolder, textFieldValue)

@Preview(showBackground = true)
@Composable
fun PreviewTextField() {
    ExpenseTextField(expenseHolder =
    ExpenseHolder.IndividualExpenseHolder(
        person = Person("ID0", "Mikkel"),
        expense = 1000F,
        isParticipant = true
    ),
        onChangeListener = {},
        onConfirm = {})
}