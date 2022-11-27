package com.mikkelthygesen.billsplit.ui.widgets.dialogs

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel


@Composable
fun ConfirmChangesDialog(
    viewModel: GroupViewModel = viewModel(),
    groupExpense: GroupExpense
) {
    GenericDialog(
        dialogText = "You made changes to the expense. Keep editing or revert changes?",
        primaryText = "Keep editing",
        primaryAction = viewModel::dismissDialog,
        secondaryText = "Revert changes and cancel",
        secondaryAction = {
            groupExpense.revertChanges()
            viewModel.dismissDialog()
            viewModel.onBackButtonPressed()
        }
    )
}
