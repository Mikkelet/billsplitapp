package com.mikkelthygesen.billsplit.features.base

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.MainViewModel
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.DialogHandler
import com.mikkelthygesen.billsplit.getActivity
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.RequireUserView
import timber.log.Timber

@Composable
fun BaseScaffoldWithAuth(
    baseViewModel: BaseViewModel,
    floatingActionButton: @Composable () -> Unit = {},
    onDismiss: () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    BaseScaffold(
        baseViewModel = baseViewModel,
        floatingActionButton = floatingActionButton,
        onDismiss = onDismiss,
        topBar = topBar,
        bottomBar = bottomBar,
    ) {
        RequireUserView(baseViewModel = baseViewModel) {
            content()
        }
    }
}

@Composable
fun BaseScaffold(
    baseViewModel: BaseViewModel,
    floatingActionButton: @Composable () -> Unit = {},
    onDismiss: () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val activity = LocalContext.current.getActivity()
    val mainViewModel: MainViewModel = if (activity != null)
        viewModel(activity) else {
        Timber.wtf("Activity is null!")
        viewModel()
    }

    BillSplitTheme {

        DialogHandler(viewModel = baseViewModel)
        DialogHandler(viewModel = mainViewModel)
        BackHandler {
            baseViewModel.onBackButtonPressed()
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