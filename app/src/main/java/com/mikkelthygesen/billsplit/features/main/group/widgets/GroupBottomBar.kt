package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel


@Composable
fun GroupBottomBar(
    uiState: BaseViewModel.UiState
) {
    when (uiState) {
        is GroupViewModel.EditExpense -> {
            val groupExpense = uiState.groupExpense
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
                Box(Modifier.weight(1f))
                Text(
                    text = "$${groupExpense.total}",
                    style = TextStyle(color = MaterialTheme.colors.onBackground)
                )
            }
        }
    }
}
