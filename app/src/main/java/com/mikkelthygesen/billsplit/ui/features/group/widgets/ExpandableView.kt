package com.mikkelthygesen.billsplit.ui.features.group.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableView(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onIconClick: () -> Unit,
    iconResId: Int,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 16.dp),
            horizontalAlignment = if (!expanded) Alignment.CenterHorizontally else Alignment.Start
        ) {
            content()
        }
        if (expanded)
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .wrapContentWidth()
                    .fillMaxHeight(),
                onClick = onIconClick,
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Edit expense",
                    tint = MaterialTheme.colors.secondary
                )
            }
    }
}

@Preview
@Composable
private fun Preview(){
    ExpandableView(onIconClick = { }, iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_money) {

    }
}