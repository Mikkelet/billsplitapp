package com.mikkelthygesen.billsplit.ui.features.main.signup.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EmailTextField(value: String, onChange: (String) -> Unit) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        placeholder = { Text(text = "Email") },
        onValueChange = onChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        isError = isValueEmail(value),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

private fun isValueEmail(email: String): Boolean {
    return email.isNotBlank() && !email.contains("@")
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EmailTextField(value = "Mikke@Email.com", onChange = {})
}