package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.ui.widgets.BackButton
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.features.base.BaseViewModel


@Composable
fun GroupTopBar(
    modifier: Modifier = Modifier,
    groupViewModel: GroupViewModel = viewModel(),
    onBackPressed: () -> Unit
) {
    val uiState = groupViewModel.uiStateFlow.collectAsState()

    Crossfade(targetState = uiState.value) {
        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                BackButton(onClick = onBackPressed)
                val title = when (it) {
                    GroupViewModel.Chat -> groupViewModel.group.nameState
                    GroupViewModel.ShowDebt -> "Debts"
                    is GroupViewModel.EditExpense -> "Expense"
                    else -> ""
                }
                Text(
                    modifier = modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.background)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    text = title,
                    style = MaterialTheme.typography.h6
                )
            }
            Row(
                Modifier.shadowModifier(
                    MaterialTheme.colors.background,
                    innerPadding = PaddingValues(0.dp),
                    cornerShape = RoundedCornerShape(90),
                    outerPadding = PaddingValues(0.dp)
                )
            ) {
                if (it is GroupViewModel.EditExpense)
                    SimpleIconButton(iconResId = R.drawable.ic_check) {
                        groupViewModel.saveGroupExpense(it.groupExpense)
                    }
                if (it is GroupViewModel.Chat)
                    SimpleIconButton(iconResId = R.drawable.ic_baseline_settings_24) {
                        // show settings
                    }
                if (it is GroupViewModel.Chat)
                    SimpleIconButton(
                        iconResId = R.drawable.ic_money,
                        onClick = groupViewModel::showDebt
                    )
            }
        }
    }
}

@Composable
fun GroupTopBar2(){
    val groupViewModel: GroupViewModel = viewModel()
    val uiStateFlow = groupViewModel.uiStateFlow.collectAsState()
    val uiState = uiStateFlow.value

    TopAppBar(
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
