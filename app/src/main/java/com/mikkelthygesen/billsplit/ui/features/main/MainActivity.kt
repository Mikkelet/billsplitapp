package com.mikkelthygesen.billsplit.ui.features.main

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.ui.features.group.GroupActivity
import com.mikkelthygesen.billsplit.ui.features.main.add_group.AddGroupView
import com.mikkelthygesen.billsplit.ui.features.main.groups.GroupsList
import com.mikkelthygesen.billsplit.ui.features.main.profile.ProfileView
import com.mikkelthygesen.billsplit.ui.features.main.signup.SignInView
import com.mikkelthygesen.billsplit.ui.features.main.signup.SignUpView
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
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
                Scaffold(
                    backgroundColor = MaterialTheme.colors.background,
                    bottomBar = { if (showNavigation(uiStateFlow.value)) BottomNavBar() },
                    topBar = {
                        if (BuildConfig.DEBUG)
                            TopAppBar {
                                Column {
                                    Text(text = "flavor=${BuildConfig.FLAVOR}")
                                    Text(text = "uid=${viewModel.loggedInUser?.uid}")
                                }
                            }
                    }
                ) { padding ->
                    Crossfade(
                        modifier = Modifier.padding(padding),
                        targetState = uiStateFlow.value
                    ) { uiState ->
                        when (uiState) {
                            BaseViewModel.UiState.SignIn -> SignInView()
                            BaseViewModel.UiState.SignUp -> SignUpView()
                            else -> MainView(
                                uiState = uiStateFlow.value,
                                dialogState = dialogStateFlow.value
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun showNavigation(uiState: BaseViewModel.UiState): Boolean {
    return when (uiState) {
        is BaseViewModel.UiState.SignUp, BaseViewModel.UiState.SignIn -> false
        else -> true
    }
}

@Composable
fun MainView(
    dialogState: BaseViewModel.DialogState,
    uiState: BaseViewModel.UiState
) {

    when (dialogState) {
        is BaseViewModel.DialogState.DismissDialogs -> Unit
    }

    when (uiState) {
        is MainViewModel.AddGroup -> AddGroupView()
        is MainViewModel.MyGroups -> GroupsList()
        is MainViewModel.ShowProfile -> ProfileView()
        is BaseViewModel.UiState.Loading -> LoadingView()
        else -> Unit
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
                Icon(painter = painterResource(id = R.drawable.ic_money), contentDescription = "")
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

@Preview(showSystemUi = true)
@Composable
private fun PreviewMainView() {
    MainView(
        dialogState = BaseViewModel.DialogState.DismissDialogs,
        uiState = MainViewModel.MyGroups
    )
}