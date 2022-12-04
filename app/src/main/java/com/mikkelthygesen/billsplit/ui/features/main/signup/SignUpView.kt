package com.mikkelthygesen.billsplit.ui.features.main.signup

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
        onSignUpWithCredentials = { x, y -> },
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
        onSignUpWithCredentials = { x, y -> },
        onSignUpWithGoogleClicked = { },
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
            Box(modifier = Modifier.defaultMinSize(16.dp).height(50.dp))
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
                        EmailTextField(value = email, onChange = {
                            email = it
                        })
                        PasswordTextField(
                            value = password,
                            onChange = { password = it })
                        if (isSignUp)
                            RepeatPasswordTextField(
                                value = repeatPassword,
                                enteredPassword = password,
                                onAction = { onSignUpWithCredentials(email, password) },
                                onChange = {
                                    repeatPassword = it
                                })
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            FlatButton(modifier = Modifier.padding(end = 16.dp),
                                text = "Sign up",
                                onClick = { onSignUpWithCredentials(email, password) })
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
