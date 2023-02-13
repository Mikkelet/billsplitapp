package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel


@Composable
fun GroupBottomBar(
    uiState: BaseViewModel.UiState
) {
    val groupViewModel: GroupViewModel = viewModel()
    NavigationBar {
        NavigationBarItem(
            selected = uiState is GroupViewModel.Chat,
            label = {
                Text(text = "Expenses")
            },
            onClick = {
                groupViewModel.showChat()
            },
            icon = {
                Icon(Icons.Filled.Add, contentDescription = "")
            })
        NavigationBarItem(
            selected = uiState is GroupViewModel.Services,
            label = {
                Text(text = "Services")
            },
            onClick = {
                groupViewModel.showServices()
            },
            icon = {
                Icon(Icons.Filled.MoreVert, contentDescription = "")
            })
        NavigationBarItem(
            selected = uiState is GroupViewModel.ShowDebt,
            label = {
                Text(text = "Debts")
            },
            onClick = {
                groupViewModel.showDebt()
            },
            icon = {
                Icon(
                    painter = painterResource(id = com.mikkelthygesen.billsplit.R.drawable.ic_money),
                    contentDescription = ""
                )
            })
    }
}


@Composable
fun TotalStatusBar(expense: GroupExpense) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Total",
            style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
        )
        Text(
            text = "$${expense.total}",
            style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}