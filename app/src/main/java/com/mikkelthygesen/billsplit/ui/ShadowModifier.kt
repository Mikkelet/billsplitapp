package com.mikkelthygesen.billsplit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

fun Modifier.shadowModifier(
    backgroundColor: Color,
    onClick: (() -> Unit)? = null,
    cornerShape: Shape = RoundedCornerShape(30.dp),
    innerPadding: PaddingValues = PaddingValues(24.dp),
    outerPadding: PaddingValues = PaddingValues(
        top = 4.dp,
        bottom = 4.dp,
        start = 16.dp,
        end = 16.dp
    )
) = padding(outerPadding)
        .clip(cornerShape)
        .background(backgroundColor)
        .let {
            if (onClick != null)
                it.clickable { onClick() }
            else it
        }
        .padding(innerPadding)


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val isLight = true
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(
                    PaddingValues(
                        top = 4.dp,
                        bottom = 4.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                )
                .clip(RoundedCornerShape(20.dp))
                .background(if (isLight) Color(0xFFE3E8E8) else Color(0xFF323232))
                .padding(PaddingValues(12.dp)),
            text = "Hello worlds"
        )
    }
}