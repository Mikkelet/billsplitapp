package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CloseButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SimpleIconButton(
        modifier = modifier,
        icon = Icons.Filled.Close,
        onClick = onClick
    )
}