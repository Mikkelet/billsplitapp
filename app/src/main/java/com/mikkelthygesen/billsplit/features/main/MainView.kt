package com.mikkelthygesen.billsplit.features.main

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.group.GroupActivity
import com.mikkelthygesen.billsplit.features.main.add_group.AddGroupView
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.ErrorDialog
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView

@Composable
fun MainView() {
    val viewModel: MainViewModel = viewModel()

    val uiStateFlow = viewModel.uiStateFlow.collectAsState()

    BillSplitTheme {

        when (val state = viewModel.dialogState) {
            is BaseViewModel.DialogState.Error -> ErrorDialog(
                exception = state.exception,
                onDismiss = viewModel::dismissDialog
            )
            is BaseViewModel.DialogState.DismissDialogs -> Unit
        }

        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            floatingActionButton = {
                Crossfade(targetState = uiStateFlow.value) { uiState ->
                    when(uiState){
                        is MainViewModel.MyGroups -> FloatingActionButton(onClick = {
                            viewModel.showAddGroup()
                        }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add Group")
                        }
                    }
                }
            },
            topBar = {
                if (BuildConfig.DEBUG)
                    TopAppBar {
                        Column {
                            Text(text = "flavor=${BuildConfig.FLAVOR}")
                            Text(text = "uid=${viewModel.loggedIdUser}")
                        }
                    }
            }
        ) { padding ->
            Crossfade(
                modifier = Modifier.padding(padding),
                targetState = uiStateFlow.value
            ) { uiState ->
                when (uiState) {
                    is BaseViewModel.UiState.Loading -> LoadingView()
                    else -> Unit
                }
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
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(
        color = MaterialTheme.colors.background,
    )
    BottomAppBar(
        modifier = Modifier,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        BottomNavigationItem(
            selected = uiState is MainViewModel.ShowProfile,
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.Gray,
            onClick = viewModel::showProfile,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_person_24),
                    contentDescription = ""
                )
            }
        )
        BottomNavigationItem(
            selected = uiState is MainViewModel.MyGroups,
            onClick = viewModel::showMyGroups,
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.Gray,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_groups_24),
                    contentDescription = ""
                )
            }
        )
    }
}