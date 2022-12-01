package com.mikkelthygesen.billsplit.ui.features.group.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.sampleSharedExpenses


@Composable
fun ExpenseViewHeader(
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

@Preview
@Composable
private fun Preview(){
    ExpenseViewHeader(groupExpense = sampleSharedExpenses.first())
}