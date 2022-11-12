package com.mikkelthygesen.billsplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.ui.features.main.AddSharedExpense
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetView
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetViewModel
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.FabView

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
                        TopAppBar {
                            Text(text = "BudgetView")
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        Crossfade(targetState = state) {
                            when (it) {
                                is SharedBudgetViewModel.UiState.ShowBudget -> FabView(onClick = viewModel::addExpense)
                                is SharedBudgetViewModel.UiState.ShowAddExpense -> {
                                    if (it.sharedExpense.getTotal() > 0)
                                        FabView(onClick = {
                                            viewModel.onAddSharedExpense(it.sharedExpense)
                                        }, R.drawable.ic_check)
                                }
                            }
                        }
                    }
                ) {
                    Crossfade(targetState = state) {
                        when (it) {
                            is SharedBudgetViewModel.UiState.ShowBudget -> {
                                SharedBudgetView(
                                    sharedExpenses = sharedExpenses.value,
                                    onFabClicked = viewModel::addExpense,
                                    onSharedExpenseClicked = {
                                        viewModel.editSharedExpense(it)
                                    }
                                )
                            }
                            is SharedBudgetViewModel.UiState.ShowAddExpense -> {
                                AddSharedExpense(
                                    sharedExpense = it.sharedExpense,
                                    addSharedExpenseCallback = viewModel
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
        when (viewModel.uiStateFlow.value) {
            is SharedBudgetViewModel.UiState.ShowBudget -> {}
            is SharedBudgetViewModel.UiState.ShowAddExpense -> {
                viewModel.showBudget()
            }
        }
    }
}

