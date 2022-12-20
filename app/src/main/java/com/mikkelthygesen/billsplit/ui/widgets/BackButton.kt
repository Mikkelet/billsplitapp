package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mikkelthygesen.billsplit.R

@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SimpleIconButton(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.background),
        iconResId = R.drawable.ic_back,
        onClick = onClick
    )
}