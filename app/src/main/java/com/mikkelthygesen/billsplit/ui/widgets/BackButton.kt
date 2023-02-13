package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SimpleIconButton(
        modifier = modifier,
        icon = Icons.Filled.ArrowBack,
        onClick = onClick
    )
}