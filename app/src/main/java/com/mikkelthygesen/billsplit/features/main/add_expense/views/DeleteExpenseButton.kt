package com.mikkelthygesen.billsplit.features.main.add_expense.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.add_expense.AddExpenseViewModel
import com.mikkelthygesen.billsplit.ui.widgets.GenericDialog
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton

@Composable
fun DeleteExpenseButton() {
    val addExpenseViewModel: AddExpenseViewModel = viewModel()
    var showConfirmDeleteDialog by remember {
        mutableStateOf(false)
    }
    if (showConfirmDeleteDialog) {
        GenericDialog(
            dialogText = "Do you want to delete this event?",
            primaryText = "Yes",
            onDismiss = { showConfirmDeleteDialog = false },
            primaryAction = { addExpenseViewModel.deleteExpense() },
            secondaryText = "No",

            )
    }
    SimpleIconButton(
        icon = Icons.Filled.Delete,
        tint = MaterialTheme.colorScheme.error
    ) {
        showConfirmDeleteDialog = true
    }

}