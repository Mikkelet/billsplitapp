package com.mikkelthygesen.billsplit.ui.features.group.widgets

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel
import com.mikkelthygesen.billsplit.ui.widgets.GenericDialog

@Composable
fun ConfirmChangesDialog(
    viewModel: GroupViewModel = viewModel(),
    groupExpense: GroupExpense
) {
    _ConfirmChangesDialog(
        dismiss = viewModel::dismissDialog,
        revert = {
            groupExpense.revertChanges()
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
        secondaryAction = revert
    )
}

@Preview
@Composable
private fun Preview(){
    _ConfirmChangesDialog(
        dismiss = {},
        revert = {}
    )
}