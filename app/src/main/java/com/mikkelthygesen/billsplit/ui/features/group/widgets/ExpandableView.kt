package com.mikkelthygesen.billsplit.ui.features.group.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.sampleSharedExpenses

@Composable
fun ExpandableView(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onIconClick: () -> Unit,
    iconResId: Int,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 12.dp, horizontal = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(5f)) {
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
                        tint = MaterialTheme.colors.secondary
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
        iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_money
    ) {
        ListViewExpense(
            groupExpense = sampleSharedExpenses.first(),
            isFocused = true,
            isLastMessage = true
        )
    }
}