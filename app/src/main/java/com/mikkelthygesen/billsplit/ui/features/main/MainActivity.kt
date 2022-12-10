package com.mikkelthygesen.billsplit.ui.features.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
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
import com.google.firebase.FirebaseApp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.ui.features.group.GroupActivity
import com.mikkelthygesen.billsplit.ui.features.main.add_group.AddGroupView
import com.mikkelthygesen.billsplit.ui.features.main.groups.GroupsList
import com.mikkelthygesen.billsplit.ui.features.main.profile.ProfileView
import com.mikkelthygesen.billsplit.ui.features.main.signup.SignInView
import com.mikkelthygesen.billsplit.ui.features.main.signup.SignUpView
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
                Scaffold(
                    backgroundColor = MaterialTheme.colors.background,
                    topBar = { if (showNavigation(uiStateFlow.value)) MainTopBar() },
                    bottomBar = { if (showNavigation(uiStateFlow.value)) BottomNavBar() }
                ) { padding ->
                    Crossfade(
                        modifier = Modifier.padding(padding),
                        targetState = uiStateFlow.value
                    ) { uiState ->
                        when (uiState) {
                            MainViewModel.SignIn -> SignInView()
                            MainViewModel.SignUp -> SignUpView()
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
        is MainViewModel.SignUp, MainViewModel.SignIn -> false
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
        is MainViewModel.AddGroup -> AddGroupView(group = uiState.group, friends = emptyList())
        is MainViewModel.MyGroups -> GroupsList()
        is MainViewModel.ShowProfile -> ProfileView(user = uiState.user, friends = uiState.friends)
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
            onClick = viewModel::addGroup,
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


@Composable
private fun MainTopBar(
    viewModel: MainViewModel = viewModel()
) {
    val uiStateFlow = viewModel.uiStateFlow.collectAsState()
    val uiState = uiStateFlow.value
    TopAppBar(
        title = { Text("Main!") },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        actions = {
            when (uiState) {
                is MainViewModel.AddGroup -> {
                    IconButton(
                        iconResId = R.drawable.ic_check,
                        color = MaterialTheme.colors.onPrimary
                    ) {
                        if (uiState.group.nameState.isNotBlank())
                            viewModel.saveGroup(uiState.group)
                    }
                }
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewMainView() {
    MainView(
        dialogState = BaseViewModel.DialogState.DismissDialogs,
        uiState = MainViewModel.AddGroup(Group("id"))
    )
}