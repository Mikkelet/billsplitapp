package com.mikkelthygesen.billsplit.ui.features.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.ui.features.group.GroupActivity
import com.mikkelthygesen.billsplit.ui.features.main.add_group.AddGroupView
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.IconButton

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    init {
        onBackPressedDispatcher.addCallback(this) {
            when (viewModel.uiStateFlow.value) {
                !is MainViewModel.Main -> viewModel.showMain()
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
                MainView(
                    uiState = uiStateFlow.value,
                    dialogState = dialogStateFlow.value
                )
            }
        }
    }
}

@Composable
fun MainView(
    dialogState: BaseViewModel.DialogState,
    uiState: BaseViewModel.UiState
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = { MainTopBar() },
        bottomBar = { BottomNavBar() }
    ) {
        when (dialogState) {
            is BaseViewModel.DialogState.DismissDialogs -> Unit
        }

        Crossfade(
            modifier = Modifier.padding(it),
            targetState = uiState
        ) { uiState ->
            when (uiState) {
                is MainViewModel.AddGroup -> AddGroupView(group = uiState.group)
                else -> Text(text = ("Hello"))
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    viewModel: MainViewModel = viewModel()
) {
    BottomAppBar(
        modifier = Modifier,
    ) {
        IconButton(iconResId = R.drawable.ic_baseline_edit_24) {
            viewModel.showMain()
        }
        IconButton(iconResId = R.drawable.ic_money) {
            viewModel.addGroup()
        }
        IconButton(iconResId = R.drawable.ic_check) {

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
                    is MainViewModel.AddGroup -> {
                        IconButton(iconResId = R.drawable.ic_check) {
                            if (state.group.nameState.isNotBlank())
                                viewModel.saveGroup(state.group)
                        }
                    }
                }
            },
            navigationIcon = {
                if (state !is MainViewModel.Main) {
                    IconButton(iconResId = R.drawable.ic_back) {
                        when (state) {
                            !is MainViewModel.Main -> viewModel.onBackButtonPressed()
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewMainView() {
    MainView(
        dialogState = BaseViewModel.DialogState.DismissDialogs,
        uiState = MainViewModel.AddGroup(Group("id"))
    )
}