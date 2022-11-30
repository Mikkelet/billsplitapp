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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.ui.features.group.GroupActivity
import com.mikkelthygesen.billsplit.ui.features.main.add_group.AddGroupView
import com.mikkelthygesen.billsplit.ui.features.main.groups.GroupsList
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.IconButton
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView

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
                is MainViewModel.Groups -> GroupsList(groups = uiState.groups)
                is BaseViewModel.UiState.Loading -> LoadingView()
                else -> Text(text = ("Hello"))
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    viewModel: MainViewModel = viewModel()
) {
    val uiStateFlow = viewModel.uiStateFlow.collectAsState()
    val uiState = uiStateFlow.value
    BottomAppBar(
        modifier = Modifier,
    ) {
        BottomNavigationItem(
            selected = uiState is MainViewModel.Main,
            onClick = viewModel::showMain,
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_search_24), contentDescription = "")
            }
        )
        BottomNavigationItem(
            selected = uiState is MainViewModel.AddGroup,
            onClick = viewModel::addGroup,
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_money), contentDescription = "")
            }
        )
        BottomNavigationItem(
            selected = uiState is MainViewModel.Groups,
            onClick = viewModel::getGroups,
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_groups_24), contentDescription = "")
            }
        )
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
                        IconButton(iconResId = R.drawable.ic_check, color = MaterialTheme.colors.onPrimary) {
                            if (state.group.nameState.isNotBlank())
                                viewModel.saveGroup(state.group)
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