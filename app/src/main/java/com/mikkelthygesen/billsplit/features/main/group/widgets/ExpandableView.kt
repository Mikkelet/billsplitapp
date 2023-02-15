package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.sampleSharedExpenses

enum class Position {
    Start,
    Middle,
    End
}

@Composable
fun ExpandableView(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onIconClick: () -> Unit,
    iconResId: Int,
    position: Position = Position.Middle,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    println("qqq rec positon=$position")
    Column(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .let { mod ->
                when (position) {
                    Position.End -> mod.clip(
                        RoundedCornerShape(
                            topEnd = MaterialTheme.shapes.extraLarge.topEnd,
                            topStart = MaterialTheme.shapes.extraLarge.topStart,
                            bottomEnd = MaterialTheme.shapes.small.bottomEnd,
                            bottomStart = MaterialTheme.shapes.small.bottomStart
                        )
                    )
                    Position.Start -> mod.clip(
                        RoundedCornerShape(
                            topEnd = MaterialTheme.shapes.small.topEnd,
                            topStart = MaterialTheme.shapes.small.topStart,
                            bottomEnd = MaterialTheme.shapes.extraLarge.bottomEnd,
                            bottomStart = MaterialTheme.shapes.extraLarge.bottomStart
                        )
                    )
                    else -> mod.clip(
                        RoundedCornerShape(
                            topEnd = MaterialTheme.shapes.small.topEnd,
                            topStart = MaterialTheme.shapes.small.topStart,
                            bottomEnd = MaterialTheme.shapes.small.bottomEnd,
                            bottomStart = MaterialTheme.shapes.small.bottomStart
                        )
                    )
                }

            }
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 16.dp, horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(5f),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                title()
            }
            if (expanded)
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(),
                    onClick = onIconClick,
                ) {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = "Edit expense",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
        }
        content()
    }
}

@Preview
@Composable
private fun PreviewExpanded() {
    ExpandableView(
        onIconClick = { },
        expanded = true,
        title = { Text(text = "My titleMy titleMy titleMytitleMy titleMy titleMy titleMy title") },
        iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_money,
    ) {
        ListViewExpense(
            groupExpense = sampleSharedExpenses.first(),
            isFocused = true,
        )
    }
}