package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun TopLoader(modifier: Modifier, show: Boolean) {
    if (show)
        CircularProgressIndicator(
            modifier = modifier
                .padding(top = 24.dp)
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.background)
                .padding(4.dp)
        )
}