package com.mikkelthygesen.billsplit.ui.features.main.profile.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun shadowModifier(backgroundColor: Color) = Modifier
    .fillMaxWidth()
    .clip(RoundedCornerShape(3.dp))
    .padding(top = 16.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
    .shadow(10.dp, RoundedCornerShape(10.dp))
    .background(backgroundColor)
    .padding(16.dp)