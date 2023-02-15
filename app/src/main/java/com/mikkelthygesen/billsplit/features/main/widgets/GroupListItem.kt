package com.mikkelthygesen.billsplit.features.main.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.fmt2dec
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.sampleGroup
import com.mikkelthygesen.billsplit.ui.theme.listItemColor
import kotlin.math.absoluteValue

@Composable
fun GroupListItem(user: Person, group: Group, onClick: (Group) -> Unit) {
    val debt = group.debts.find { it.first == user.uid } ?: Pair(user.uid, 0F)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadowModifier(
                backgroundColor = listItemColor(),
                cornerShape = MaterialTheme.shapes.extraLarge,
                onClick = { onClick(group) },
                outerPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                innerPadding = PaddingValues(32.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = group.nameState,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp)
            )
            Text(text = "${group.latestEvent?.timeStamp}")
        }
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