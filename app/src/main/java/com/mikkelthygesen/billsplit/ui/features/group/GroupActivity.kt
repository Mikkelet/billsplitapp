package com.mikkelthygesen.billsplit.ui.features.group

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.ui.features.group.add_expense.ExpenseView
import com.mikkelthygesen.billsplit.ui.features.group.group.GroupEventsView
import com.mikkelthygesen.billsplit.ui.features.group.view_expenses.ViewExpenses
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.IconButton
import com.mikkelthygesen.billsplit.ui.widgets.dialogs.ConfirmChangesDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupActivity : ComponentActivity() {

    private val viewModel: GroupViewModel by viewModels()
    private val groupId by lazy { intent.extras?.getString("group_id") ?: "" }

    init {
        onBackPressedDispatcher.addCallback(this) {
            when (val state = viewModel.uiStateFlow.value) {
                is GroupViewModel.ShowExpense -> {
                    if (state.groupExpense.isChanged())
                        viewModel.showConfirmChangesDialog(state.groupExpense)
                    else viewModel.showEvents()
                }
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
                val dialogState = viewModel.dialogState.collectAsState()
                val uiState = groupUiState.value

                LaunchedEffect(Unit) {
                    viewModel.uiEventsState.collect { event ->
                        when (event) {
                            is BaseViewModel.UiEvent.OnBackPressed -> onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                val text = when (uiState) {
                                    is GroupViewModel.Events -> ""
                                    is GroupViewModel.ShowExpense -> "Expenses for LOGGED IN"
                                    is GroupViewModel.ShowDebt -> "Add new expense"
                                    else -> ""
                                }
                                Text(text)
                            },
                            backgroundColor = MaterialTheme.colors.background,
                            contentColor = MaterialTheme.colors.onBackground,
                            actions = {
                                when (uiState) {
                                    is GroupViewModel.Events -> {
                                        IconButton(
                                            iconResId = R.drawable.ic_money,
                                            onClick = viewModel::showDebt
                                        )
                                    }
                                    is GroupViewModel.ShowExpense ->
                                        if (uiState.groupExpense.total > 0)
                                            IconButton(iconResId = R.drawable.ic_check) {
                                                viewModel.saveGroupExpense(uiState.groupExpense)
                                            }
                                    else -> {}
                                }
                            },
                            navigationIcon = {
                                if (uiState !is GroupViewModel.Events) {
                                    IconButton(iconResId = R.drawable.ic_back) {
                                        when (uiState) {
                                            is GroupViewModel.ShowExpense -> {
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
                                }
                            })
                    }
                ) {
                    when (val state = dialogState.value) {
                        is GroupViewModel.ConfirmChangesDialog -> ConfirmChangesDialog(groupExpense = state.groupExpense)
                        is BaseViewModel.DialogState.DismissDialogs -> Unit
                    }
                    when (uiState) {
                        is GroupViewModel.Events -> GroupEventsView(modifier = Modifier.padding(it))
                        is GroupViewModel.ShowDebt -> ViewExpenses()
                        is GroupViewModel.ShowExpense -> ExpenseView(groupExpense = uiState.groupExpense)
                        is BaseViewModel.UiState.Loading -> Box(Modifier.fillMaxSize()) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

}