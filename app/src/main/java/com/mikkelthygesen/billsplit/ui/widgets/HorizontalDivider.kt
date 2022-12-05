package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HorizontalDivider(modifier: Modifier = Modifier) {
    Box(
        modifier
            .padding(8.dp)
    ){
        Divider()
    }
}