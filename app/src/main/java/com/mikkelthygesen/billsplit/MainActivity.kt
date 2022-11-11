package com.mikkelthygesen.billsplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.ui.features.main.AddSharedExpense
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetView
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetViewModel
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme

class MainActivity : ComponentActivity() {

    private val viewModel = SharedBudgetViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState = viewModel.uiStateFlow.collectAsState()
            val sharedExpenses = viewModel.sharedExpensesState.collectAsState()
            BillSplitTheme {
                val state = uiState.value
                when (state) {
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
                        var editable by remember { mutableStateOf(true) }
                        val density = LocalDensity.current
                        AnimatedVisibility(visible = editable, enter =
                        slideInVertically {
                            with(density) {
                                -40.dp.roundToPx()
                            }
                        }) {
                            AddSharedExpense(
                                participants = state.sharedExpense.people,
                                sharedExpenses = state.sharedExpense.sharedExpense,
                                addSharedExpenseCallback = viewModel
                            )
                    }
                }
            }

        }
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

