package com.mikkelthygesen.billsplit.ui.features.main.signup

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.matchesEmail
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.signup.widgets.EmailTextField
import com.mikkelthygesen.billsplit.ui.features.main.signup.widgets.PasswordTextField
import com.mikkelthygesen.billsplit.ui.features.main.signup.widgets.RepeatPasswordTextField
import com.mikkelthygesen.billsplit.ui.widgets.FlatButton

@Composable
fun SignUpView(
    mainViewModel: MainViewModel = viewModel()
) {
    _SignUpView(
        onSignUpWithCredentials = { email, password ->
            mainViewModel.signUpEmail(email, password)
        },
        onSignUpWithGoogleClicked = { },
        onSignUpWithFacebookClicked = {},
        onAlreadySignedUpClicked = mainViewModel::showSignIn,
        isSignUp = true
    )
}

@Composable
fun SignInView(
    mainViewModel: MainViewModel = viewModel()
) {
    _SignUpView(
        onSignUpWithCredentials = { email, password -> mainViewModel.signInEmail(email, password) },
        onSignUpWithGoogleClicked = {},
        onSignUpWithFacebookClicked = {},
        onAlreadySignedUpClicked = mainViewModel::showSignUp
    )
}


@SuppressLint("ComposableNaming")
@Composable
private fun _SignUpView(
    onSignUpWithCredentials: (String, String) -> Unit,
    onSignUpWithGoogleClicked: () -> Unit,
    onSignUpWithFacebookClicked: () -> Unit,
    onAlreadySignedUpClicked: () -> Unit,
    isSignUp: Boolean = false
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var repeatPasswordError by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val title = if (isSignUp) "Create your new account" else "Sign in!"
    val redirectText = if (isSignUp) "I already have an account" else "I want to create an account"
    Scaffold(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(scrollState)
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h4.copy(textAlign = TextAlign.Center)
            )
            Box(
                modifier = Modifier
                    .defaultMinSize(16.dp)
                    .height(50.dp)
            )
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Column(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colors.primary,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(8.dp)
                            .wrapContentWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        EmailTextField(value = email, onChange = { value ->
                            email = value
                        }, hasError = emailError)
                        PasswordTextField(
                            value = password,
                            onChange = { value -> password = value },
                            hasError = passwordError
                        )
                        if (isSignUp)
                            RepeatPasswordTextField(
                                value = repeatPassword,
                                onAction = { onSignUpWithCredentials(email, password) },
                                onChange = { value -> repeatPassword = value },
                                hasError = repeatPasswordError
                            )
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            FlatButton(modifier = Modifier.padding(end = 16.dp),
                                text = if (isSignUp) "Sign up" else "Sign in",
                                onClick = {
                                    emailError = when {
                                        email.isBlank() -> "Enter your email"
                                        !email.matchesEmail() -> "Not a valid email"
                                        else -> ""
                                    }
                                    passwordError = when {
                                        password.isBlank() -> "Enter password"
                                        password.length < 6 -> "Password must be at least 6 characters long"
                                        else -> ""
                                    }
                                    repeatPasswordError = when {
                                        repeatPassword.isBlank() -> "Repeat your password"
                                        password != repeatPassword -> "Passwords are not identical"
                                        else -> ""
                                    }
                                    if (passwordError.isBlank()
                                        && repeatPasswordError.isBlank()
                                        && emailError.isBlank()
                                    )
                                        onSignUpWithCredentials(
                                            email.trim().lowercase(),
                                            password
                                        )
                                })
                        }
                    }
                    Text(modifier = Modifier.padding(vertical = 16.dp), text = "⸺ OR  ⸺")
                    Button(
                        modifier = Modifier.padding(bottom = 16.dp),
                        onClick = onSignUpWithGoogleClicked
                    ) {
                        Text(text = "Sign up with Google")
                    }
                    Button(
                        modifier = Modifier.padding(bottom = 16.dp),
                        onClick = onSignUpWithFacebookClicked
                    ) {
                        Text(text = "Sign up with Facebook")
                    }
                    FlatButton(
                        text = redirectText,
                        onClick = onAlreadySignedUpClicked,
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    _SignUpView(
        onSignUpWithCredentials = { x, y -> },
        onSignUpWithGoogleClicked = { },
        onSignUpWithFacebookClicked = {},
        onAlreadySignedUpClicked = {},
        isSignUp = true
    )
}
