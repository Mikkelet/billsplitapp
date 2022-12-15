package com.mikkelthygesen.billsplit.ui.features.main.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.Group

@Composable
fun GroupListItem(group: Group) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        Arrangement.SpaceBetween
    ) {
        Text(text = group.nameState)
        Icon(painter = painterResource(id = R.drawable.ic_money), contentDescription = "")
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Box(
        Modifier.padding(8.dp)
    ) {
        GroupListItem(group = Group("", "My Group"))
    }
}