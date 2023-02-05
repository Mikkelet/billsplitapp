package com.mikkelthygesen.billsplit.features.main.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BigTopBar(
    modifier: Modifier = Modifier,
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(80.dp)
        ,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            leadingContent()
        }
        Row {
            trailingContent()
        }
    }
}