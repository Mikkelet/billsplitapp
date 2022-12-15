package com.mikkelthygesen.billsplit.ui.features.group

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.ui.features.group.add_expense.ExpenseView
import com.mikkelthygesen.billsplit.ui.features.group.group_view.GroupEventsView
import com.mikkelthygesen.billsplit.ui.features.group.view_expenses.ViewExpenses
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.IconButton
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import com.mikkelthygesen.billsplit.ui.features.group.widgets.ConfirmChangesDialog
import com.mikkelthygesen.billsplit.ui.features.main.widgets.dialogs.ErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupActivity : ComponentActivity() {

    private val viewModel: GroupViewModel by viewModels()
    private val groupId by lazy { intent.extras?.getString("group_id") ?: "" }

    init {
        onBackPressedDispatcher.addCallback(this) {
            when (val state = viewModel.uiStateFlow.value) {
                is GroupViewModel.EditExpense -> {
                    if (state.groupExpense.isChanged())
                        viewModel.showConfirmChangesDialog(state.groupExpense)
                    else viewModel.showEvents()
                }
                is GroupViewModel.ShowDebt -> viewModel.showEvents()
                else -> finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getGroup(groupId)

        setContent {

            BillSplitTheme {
                val groupUiState = viewModel.uiStateFlow.collectAsState()
                val uiState = groupUiState.value

                when (val state = viewModel.dialogState) {
                    is GroupViewModel.ConfirmChangesDialog -> ConfirmChangesDialog(groupExpense = state.groupExpense)
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
                        }
                    }
                }

                Scaffold(
                    topBar = { TopBar() },
                    bottomBar = { BottomBar(uiState) }
                ) {
                    Crossfade(targetState = uiState, modifier = Modifier.padding(it)) { state ->
                        when (state) {
                            is BaseViewModel.UiState.Loading -> LoadingView()
                            is GroupViewModel.Expenses -> GroupEventsView()
                            is GroupViewModel.ShowDebt -> ViewExpenses(user = state.user)
                            is GroupViewModel.EditExpense -> ExpenseView(groupExpense = state.groupExpense)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    viewModel: GroupViewModel = viewModel()
) {
    val uiStateFlow = viewModel.uiStateFlow.collectAsState()
    val uiState = uiStateFlow.value
    TopAppBar(
        title = {
            val text = when (uiState) {
                is GroupViewModel.Expenses -> ""
                is GroupViewModel.EditExpense -> "Expenses for LOGGED IN"
                is GroupViewModel.ShowDebt -> "Add new expense"
                else -> ""
            }
            Text(text)
        },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        actions = {
            when (uiState) {
                is GroupViewModel.Expenses -> {
                    IconButton(iconResId = R.drawable.ic_baseline_settings_24) {
                        // show settings
                    }
                    IconButton(
                        iconResId = R.drawable.ic_money,
                        onClick = viewModel::showDebt
                    )
                }
                is GroupViewModel.EditExpense ->
                    if (uiState.groupExpense.total > 0)
                        IconButton(iconResId = R.drawable.ic_check) {
                            viewModel.saveGroupExpense(uiState.groupExpense)
                        }
                else -> {}
            }
        },
        navigationIcon = {
            IconButton(iconResId = R.drawable.ic_back) {
                when (uiState) {
                    is GroupViewModel.EditExpense -> {
                        val groupExpense = uiState.groupExpense
                        if (!groupExpense.isChanged())
                            viewModel.onBackButtonPressed()
                        else {
                            viewModel.showConfirmChangesDialog(uiState.groupExpense)
                        }
                    }
                    else -> viewModel.onBackButtonPressed()
                }
            }
        })
}


@Composable
private fun BottomBar(
    uiState: BaseViewModel.UiState
) {
    when (uiState) {
        is GroupViewModel.EditExpense -> {
            val groupExpense = uiState.groupExpense
            Row(
                Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(8.dp),
                Arrangement.Center
            ) {
                Text(
                    text = "Total",
                    style = TextStyle(color = MaterialTheme.colors.onBackground)
                )
                Box(Modifier.weight(1f))
                Text(
                    text = "$${groupExpense.total}",
                    style = TextStyle(color = MaterialTheme.colors.onBackground)
                )
            }
        }
        else -> Unit
    }
}