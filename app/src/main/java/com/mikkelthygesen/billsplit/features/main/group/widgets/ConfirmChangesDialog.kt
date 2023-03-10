package com.mikkelthygesen.billsplit.features.main.group.widgets

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.add_expense.AddExpenseViewModel
import com.mikkelthygesen.billsplit.ui.widgets.GenericDialog

@Composable
fun ConfirmChangesDialog() {
    val viewModel: AddExpenseViewModel = viewModel()
    _ConfirmChangesDialog(
        dismiss = viewModel::dismissDialog,
        revert = {
            viewModel.groupExpense.revertChanges()
            viewModel.dismissDialog()
            viewModel.onBackButtonPressed()
        })
}

@SuppressLint("ComposableNaming")
@Composable
private fun _ConfirmChangesDialog(
    dismiss: () -> Unit,
    revert: () -> Unit
) {
    GenericDialog(
        dialogText = "You made changes to the expense. Keep editing or revert changes?",
        primaryText = "Keep editing",
        primaryAction = dismiss,
        secondaryText = "Revert changes and cancel",
        secondaryAction = revert,
        onDismiss = dismiss
    )
}

@Preview
@Composable
private fun Preview() {
    _ConfirmChangesDialog(
        dismiss = {},
        revert = {}
    )
}