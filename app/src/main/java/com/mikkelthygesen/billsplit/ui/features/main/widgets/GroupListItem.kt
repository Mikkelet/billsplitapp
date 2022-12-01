package com.mikkelthygesen.billsplit.ui.features.main.widgets

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.models.Group

@Composable
fun GroupListItem(onClick: () -> Unit, group: Group) {
    Button(onClick = onClick) {
        Text(text = group.nameState)
    }
}

@Preview
@Composable
private fun Preview(){
    GroupListItem(onClick = { }, group = Group("","My Group"))
}