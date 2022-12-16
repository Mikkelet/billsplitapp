package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun SimpleIconButton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    iconResId: Int,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(
                id = iconResId
            ),
            contentDescription = "",
            tint = color
        )
    }
}