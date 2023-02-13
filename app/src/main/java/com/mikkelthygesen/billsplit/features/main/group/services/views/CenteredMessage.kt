package com.mikkelthygesen.billsplit.features.main.group.services.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.ui.widgets.Center

@Composable
fun CenteredMessage(text: String) {
    Center(Modifier.padding(16.dp)) {
        Text(
            text = text,
            style = TextStyle(
                fontStyle = FontStyle.Italic,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        )
    }
}