package com.mikkelthygesen.billsplit.ui.features.main.signup.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.R

@Composable
fun PasswordTextField(
    value: String,
    onChange: (String) -> Unit
) {
    _PasswordTextField(
        value = value,
        placeHolder = "Password",
        onChange = onChange,
        isError = { value.length < 6 }
    )
}

@Composable
fun RepeatPasswordTextField(
    value: String,
    enteredPassword: String,
    onAction: () -> Unit,
    onChange: (String) -> Unit
) {
    _PasswordTextField(
        value = value,
        keyboardActions = KeyboardActions {
            onAction()
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        placeHolder = "Confirm password",
        isError = { value != enteredPassword },
        onChange = onChange
    )
}


@Composable
@SuppressLint("ComposableNaming")
private fun _PasswordTextField(
    value: String,
    placeHolder: String,
    isError: () -> Boolean,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onChange: (String) -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onChange,
        placeholder = {
            Text(text = placeHolder)
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (showPassword)
                painterResource(id = R.drawable.ic_baseline_visibility_24)
            else painterResource(id = R.drawable.ic_baseline_visibility_off_24)
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    icon,
                    contentDescription = "Visibility",
                    tint = Color.Gray
                )
            }
        },
        isError = value.isNotBlank() && isError(),
        singleLine = true,
        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PasswordPreview() {
    PasswordTextField(value = "mypassword", onChange = {})
}

@Preview(showBackground = true)
@Composable
private fun RepeatPasswordPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        RepeatPasswordTextField(
            value = "mypassword",
            onChange = {},
            enteredPassword = "mypassowrd",
            onAction = {})
    }
}