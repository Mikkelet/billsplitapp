package com.mikkelthygesen.billsplit.features.main.profile.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.shadowModifier(
    backgroundColor: Color,
    onClick: (() -> Unit)? = null,
    cornerShape: RoundedCornerShape = RoundedCornerShape(10.dp),
    innerPadding: PaddingValues = PaddingValues(12.dp),
    outerPadding: PaddingValues = PaddingValues(
        top = 8.dp,
        bottom = 16.dp,
        start = 16.dp,
        end = 16.dp
    )
) =
    clip(cornerShape)
        .padding(outerPadding)
        .shadow(10.dp, cornerShape)
        .background(backgroundColor)
        .let {
            if (onClick != null)
                it.clickable { onClick() }
            else it
        }
        .padding(innerPadding)