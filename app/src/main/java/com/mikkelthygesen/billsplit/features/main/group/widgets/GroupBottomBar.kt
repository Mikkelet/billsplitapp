package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
        elevation = 0.dp
    ) {
        BottomNavigationItem(
            selected = uiState is GroupViewModel.Chat,
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.Gray,
            label = {
                Text(text = "Expenses")
            },
            onClick = {
                groupViewModel.showChat()
            },
            icon = {
                Icon(Icons.Filled.Add, contentDescription = "")

            })
        BottomNavigationItem(
            selected = uiState is GroupViewModel.Services,
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.Gray,
            label = {
                Text(text = "Services")
            },
            onClick = {
                groupViewModel.showServices()
            },
            icon = {
                Icon(Icons.Filled.MoreVert, contentDescription = "")
            })
    }
}


@Composable
fun TotalStatusBar(expense: GroupExpense) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Total",
            style = TextStyle(color = MaterialTheme.colors.onBackground)
        )
        Text(
            text = "$${expense.total}",
            style = TextStyle(color = MaterialTheme.colors.onBackground)
        )
    }
}