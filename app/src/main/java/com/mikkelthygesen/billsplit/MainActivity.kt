package com.mikkelthygesen.billsplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.ui.features.main.AddSharedExpense
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetView
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetViewModel
import com.mikkelthygesen.billsplit.ui.features.view_expenses.ViewExpenses
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.FabView
import com.mikkelthygesen.billsplit.ui.widgets.IconButton

class MainActivity : ComponentActivity() {

    private val viewModel = SharedBudgetViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState = viewModel.uiStateFlow.collectAsState()
            val sharedExpenses = viewModel.sharedExpensesState.collectAsState()
            BillSplitTheme {
                val state = uiState.value
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                val text = when (state) {
                                    is SharedBudgetViewModel.UiState.ShowBudget -> "Expenses"
                                    is SharedBudgetViewModel.UiState.ViewExpense -> "Expenses for ${viewModel.getLoggedIn().name}"
                                    is SharedBudgetViewModel.UiState.ShowAddExpense -> "Add new expense"
                                }
                                Text(text)
                            },
                            actions = {
                                when (state) {
                                    is SharedBudgetViewModel.UiState.ShowBudget ->
                                        IconButton(R.drawable.ic_money, viewModel::showExpenses)
                                    is SharedBudgetViewModel.UiState.ShowAddExpense ->
                                        if (state.sharedExpense.getTotal() > 0)
                                            IconButton(R.drawable.ic_check) {
                                                viewModel.onAddSharedExpense(state.sharedExpense)
                                            }
                                    else -> {}
                                }
                            },
                            navigationIcon = {
                                if (state !is SharedBudgetViewModel.UiState.ShowBudget) {
                                    IconButton(R.drawable.ic_back, this::onBackPressed)
                                }
                            }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        Crossfade(targetState = state) {
                            when (it) {
                                is SharedBudgetViewModel.UiState.ShowBudget -> FabView(onClick = viewModel::addExpense)
                                else -> {}
                            }
                        }
                    }
                ) {
                    Crossfade(targetState = state) {
                        when (it) {
                            is SharedBudgetViewModel.UiState.ShowBudget -> {
                                SharedBudgetView(
                                    sharedExpenses = sharedExpenses.value,
                                    onSharedExpenseClicked = viewModel::editSharedExpense
                                )
                            }
                            is SharedBudgetViewModel.UiState.ShowAddExpense -> {
                                AddSharedExpense(
                                    sharedExpense = it.sharedExpense,
                                    addSharedExpenseCallback = viewModel
                                )
                            }
                            is SharedBudgetViewModel.UiState.ViewExpense -> {
                                ViewExpenses(
                                    person = viewModel.getLoggedIn(),
                                    people = viewModel.people,
                                    expenses = sharedExpenses.value
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun slideUp(density: Density) = slideInVertically {
        with(density) {
            +50.dp.roundToPx()
        }
    }

    private fun slideDown(density: Density) = slideOutVertically {
        with(density) {
            -50.dp.roundToPx()
        }
    }

    override fun onBackPressed() {
        if (viewModel.uiStateFlow.value !is SharedBudgetViewModel.UiState.ShowBudget) {
            viewModel.showBudget()
        }
    }
}

