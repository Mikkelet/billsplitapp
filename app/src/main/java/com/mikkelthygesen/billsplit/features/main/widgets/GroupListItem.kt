package com.mikkelthygesen.billsplit.features.main.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.fmt2dec
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.sampleGroup
import kotlin.math.absoluteValue

@Composable
fun GroupListItem(user: Person, group: Group, onClick: (Group) -> Unit) {
    val debt = group.debtsState.find { it.first == user.uid } ?: Pair(user.uid, 0F)
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            style = MaterialTheme.typography.body2.copy(fontSize = 18.sp)
        )
        when {
            debt.second > 0F -> Text(
                text = "-$${debt.second.fmt2dec()}",
                style = TextStyle(color = Color.Red)
            )
            debt.second < 0F -> Text(
                text = "$${debt.second.absoluteValue.fmt2dec()}",
                style = TextStyle(color = Color(0xFF0B9D3A))
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Box(
        Modifier.padding(8.dp)
    ) {
        GroupListItem(sampleGroup.createdBy, group = sampleGroup) {}
    }
}