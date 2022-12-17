package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mikkelthygesen.billsplit.R

@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SimpleIconButton(
        modifier = modifier
            .clip(RoundedCornerShape(90))
            .background(MaterialTheme.colors.background),
        iconResId = R.drawable.ic_back,
        onClick = onClick
    )
}