package com.mikkelthygesen.billsplit.features.main.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BigTopBar(
    modifier: Modifier = Modifier,
    leadingContent: @Composable () -> Unit = {},
    title: String = "",
    trailingContent: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            leadingContent()
            Text(
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp),
                text = title
            )
        }
        Row {
            trailingContent()
        }
    }
}