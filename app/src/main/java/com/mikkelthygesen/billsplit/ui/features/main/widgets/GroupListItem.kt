package com.mikkelthygesen.billsplit.ui.features.main.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.shadowModifier

@Composable
fun GroupListItem(group: Group, onClick: (Group) -> Unit) {
    Row(
        modifier = Modifier
            .shadowModifier(
                backgroundColor = MaterialTheme.colors.background,
                onClick = { onClick(group) },
                innerPadding = PaddingValues(32.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = group.nameState,
            style = MaterialTheme.typography.body2.copy(fontSize = 18.sp))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Box(
        Modifier.padding(8.dp)
    ) {
        GroupListItem(group = Group("", "My Group")) {}
    }
}