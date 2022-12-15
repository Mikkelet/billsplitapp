package com.mikkelthygesen.billsplit.ui.features.main.signup.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EmailTextField(
    value: String,
    hasError: String,
    onChange: (String) -> Unit
) {
    Column {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            placeholder = { Text(text = "Email") },
            onValueChange = onChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            isError = hasError.isNotBlank(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        if (hasError.isNotBlank())
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                text = hasError,
                style = TextStyle(color = MaterialTheme.colors.error)
            )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EmailTextField(value = "Mikke@Email.com", onChange = {}, hasError = "ERROR")
}