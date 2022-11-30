package com.mikkelthygesen.billsplit.ui.features.main.groups

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.models.Group

@Composable
fun GroupListItem(onClick: () -> Unit, group: Group) {
    Button(onClick = onClick) {
        Text(text = group.nameState)
    }
}