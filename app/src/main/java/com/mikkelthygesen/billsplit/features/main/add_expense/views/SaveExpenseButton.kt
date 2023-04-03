package com.mikkelthygesen.billsplit.features.main.add_expense.views

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.features.main.add_expense.AddExpenseViewModel
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton

@Composable
fun SaveExpenseButton() {
    val addExpenseViewModel: AddExpenseViewModel = viewModel()
    SimpleIconButton(iconResId = R.drawable.ic_check) {
        addExpenseViewModel.saveExpense()
    }
}