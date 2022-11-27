package com.mikkelthygesen.billsplit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.ui.features.group.GroupActivity
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.IconButton

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    init {
        onBackPressedDispatcher.addCallback(this) {
            when (viewModel.uiStateFlow.value) {
                !is MainViewModel.Main -> viewModel.onBackButtonPressed()
                else -> finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiStateFlow = viewModel.uiStateFlow.collectAsState()
            val dialogStateFlow = viewModel.dialogState.collectAsState()

            BillSplitTheme {
                val uiState = uiStateFlow.value
                LaunchedEffect(Unit) {
                    viewModel.uiEventsState.collect { event ->
                        when (event) {
                            is BaseViewModel.UiEvent.OnBackPressed -> onBackPressedDispatcher.onBackPressed()
                            is MainViewModel.ShowGroup -> {
                                val intent = Intent(this@MainActivity, GroupActivity::class.java)
                                intent.putExtra("group_id", event.groupId)
                                startActivity(intent)
                            }
                        }
                    }
                }

                Scaffold(
                    backgroundColor = MaterialTheme.colors.background,
                    topBar = { MainTopBar() },
                ) {
                    when (dialogStateFlow.value) {
                        is BaseViewModel.DialogState.DismissDialogs -> Unit
                    }

                    Crossfade(
                        modifier = Modifier.padding(it),
                        targetState = uiState
                    ) { uiState ->
                        when (uiState) {
                            is MainViewModel.Main -> MainView()
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun MainTopBar(
    viewModel: MainViewModel = viewModel()
) {
    val uiStateFlow = viewModel.uiStateFlow.collectAsState()
    val uiState = uiStateFlow.value
    Crossfade(targetState = uiState) { state ->
        TopAppBar(
            title = { Text("Main!") },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            actions = {
                when (state) {
                    is MainViewModel.Main -> {
                        IconButton(
                            iconResId = R.drawable.ic_money,
                            onClick = { viewModel.showGroup("") }
                        )
                    }
                    else -> {}
                }
            },
            navigationIcon = {
                if (state !is MainViewModel.Main) {
                    IconButton(iconResId = R.drawable.ic_back) {
                        when (state) {
                            !is MainViewModel.Main -> viewModel.onBackButtonPressed()
                            else -> {}
                        }
                    }
                }
            }
        )
    }
}