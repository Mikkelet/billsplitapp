package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun IconButton(iconResId: Int, onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(painter = painterResource(id = iconResId), contentDescription = "")
    }
}

@Composable
fun IconButton(modifier: Modifier, iconResId: Int, color: Color, onClick: () -> Unit) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(painter = painterResource(id = iconResId), contentDescription = "", tint = color)
    }
}