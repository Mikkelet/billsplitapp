package com.mikkelthygesen.billsplit.ui.features.main.landing

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel

@Composable
fun LandingView(
    mainViewModel: MainViewModel = viewModel()
) {
    val uiStateFlow = mainViewModel.uiStateFlow.collectAsState()
    Scaffold { padding ->
        Crossfade(
            modifier = Modifier.padding(padding),
            targetState = uiStateFlow.value
        ) { state ->
            when (state) {
                MainViewModel.SignIn -> Unit
                MainViewModel.SignUp -> Unit
                else -> {
                    Box(contentAlignment = Alignment.Center) {
                        Column {
                            Button(onClick = { }) {
                                Text(text = "Sign up!")
                            }
                            Button(onClick = { }) {
                                Text(text = "Sign in")
                            }
                        }
                    }
                }
            }
        }
    }
}

