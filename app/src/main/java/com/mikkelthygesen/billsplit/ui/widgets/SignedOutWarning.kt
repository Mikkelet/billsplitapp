package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SignedOutWarning(onClick: () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            Text(text = "It seems you've been signed out, please sign in again")
            FlatButton(text = "Sign in") {
                onClick()
            }
        }
    }
}