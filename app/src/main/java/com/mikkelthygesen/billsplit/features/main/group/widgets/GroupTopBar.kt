package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton

@Composable
fun GroupTopBar2(){
    val groupViewModel: GroupViewModel = viewModel()
    val uiStateFlow = groupViewModel.uiStateFlow.collectAsState()
    val uiState = uiStateFlow.value

    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        title = {
            if (uiState is BaseViewModel.UiState.Loading)
                Text(text = "")
            else Text(text = groupViewModel.group.nameState)
        },
        navigationIcon = {
            IconButton(onClick = {
                groupViewModel.onBackButtonPressed()
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "")
            }
        },
        actions = {
            if (uiState is GroupViewModel.Chat)
                SimpleIconButton(
                    iconResId = R.drawable.ic_baseline_settings_24,
                ) {
                    // show settings
                }
        }
    )
}
