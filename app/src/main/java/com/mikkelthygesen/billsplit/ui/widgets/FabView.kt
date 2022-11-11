package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.mikkelthygesen.billsplit.R

@Composable
fun FabView(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add_plus), contentDescription = "Add"
        )
    }
}