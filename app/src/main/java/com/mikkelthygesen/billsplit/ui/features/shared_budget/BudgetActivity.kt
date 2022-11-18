package com.mikkelthygesen.billsplit.ui.features.shared_budget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState

class BudgetActivity : ComponentActivity() {

    private val sharedBudgetViewModel = SharedBudgetViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedExpenseState = sharedBudgetViewModel.sharedExpensesState.collectAsState()
            SharedBudgetView(
                sharedExpenses = sharedExpenseState.value,
                onSharedExpenseClicked = {
                    sharedBudgetViewModel.editSharedExpense(it)
                }
            )
        }
    }
}