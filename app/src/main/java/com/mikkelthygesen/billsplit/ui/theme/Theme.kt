package com.mikkelthygesen.billsplit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val m3LightColorScheme = lightColorScheme()
private val m3DarkColorScheme = darkColorScheme()

@Composable
fun BillSplitTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        m3DarkColorScheme
    } else {
        m3LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}

@Composable
fun listItemColor():Color = MaterialTheme.colorScheme.secondaryContainer