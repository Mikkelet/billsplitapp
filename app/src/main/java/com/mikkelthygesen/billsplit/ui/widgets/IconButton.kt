package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun IconButton(iconResId: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(painter = painterResource(id = iconResId), contentDescription = "")
    }
}