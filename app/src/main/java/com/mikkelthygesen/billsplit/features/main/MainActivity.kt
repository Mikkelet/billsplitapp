package com.mikkelthygesen.billsplit.features.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
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
import com.mikkelthygesen.billsplit.features.main.groups.GroupsList
import com.mikkelthygesen.billsplit.features.main.profile.ProfileView
import com.mikkelthygesen.billsplit.features.main.signup.SignInView
import com.mikkelthygesen.billsplit.features.main.signup.SignUpView
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.ErrorDialog
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    init {
        onBackPressedDispatcher.addCallback(this) {
            when (viewModel.uiStateFlow.value) {
                !is MainViewModel.Main -> viewModel.showMyGroups()
                else -> finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiStateFlow = viewModel.uiStateFlow.collectAsState()

            BillSplitTheme {

                when (val state = viewModel.dialogState) {
                    is BaseViewModel.DialogState.Error -> ErrorDialog(
                        exception = state.exception,
                        onDismiss = viewModel::dismissDialog
                    )
                    is BaseViewModel.DialogState.DismissDialogs -> Unit
                }

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
                    bottomBar = { if (showNavigation(uiStateFlow.value)) BottomNavBar() },
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
                            is BaseViewModel.UiState.SignIn -> SignInView()
                            is BaseViewModel.UiState.SignUp -> SignUpView()
                            is MainViewModel.AddGroup -> AddGroupView()
                            is MainViewModel.MyGroups -> GroupsList()
                            is MainViewModel.ShowProfile -> ProfileView()
                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}

private fun showNavigation(uiState: BaseViewModel.UiState): Boolean {
    return when (uiState) {
        is BaseViewModel.UiState.SignUp,
        BaseViewModel.UiState.SignIn -> false
        else -> true
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
            selected = uiState is MainViewModel.AddGroup,
            onClick = viewModel::showAddGroup,
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.Gray,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_plus),
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