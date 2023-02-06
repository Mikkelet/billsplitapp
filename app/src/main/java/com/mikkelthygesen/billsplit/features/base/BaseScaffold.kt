package com.mikkelthygesen.billsplit.features.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.ErrorDialog
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme

@Composable
fun BaseScaffold(
    baseViewModel: BaseViewModel,
    floatingActionButton: @Composable () -> Unit = {},
    onDismiss: () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    BillSplitTheme {
        when (val dialogState = baseViewModel.dialogState) {
            is BaseViewModel.DialogState.Error ->
                ErrorDialog(
                    exception = dialogState.exception,
                    onDismiss = baseViewModel::dismissDialog
                )
        }

        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
        ) {
            Box(
                Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                content()
            }
        }
    }
}