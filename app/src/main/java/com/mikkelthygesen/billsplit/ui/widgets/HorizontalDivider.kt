package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HorizontalDivider(modifier: Modifier = Modifier) {
    Box(
        modifier
            .padding(8.dp)
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.onPrimary.copy(alpha = 0.5f))
    )
}