package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.main.widgets.BigTopBar
import com.mikkelthygesen.billsplit.ui.widgets.BackButton
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton

@Composable
fun GroupTopBar2() {
    val groupViewModel: GroupViewModel = viewModel()
    val uiStateFlow = groupViewModel.uiStateFlow.collectAsState()
    val uiState = uiStateFlow.value

    BigTopBar(
        title = if (uiState is BaseViewModel.UiState.Loading)
            "" else groupViewModel.group.nameState,
        leadingContent = {
            BackButton {
                groupViewModel.onBackButtonPressed()
            }
        },
        trailingContent = {
            if (uiState is BaseViewModel.UiState.Main)
                SimpleIconButton(
                    iconResId = R.drawable.ic_baseline_settings_24,
                ) {
                    // show settings
                }
        }
    )
}
