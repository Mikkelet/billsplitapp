package com.mikkelthygesen.billsplit.features.main.profile.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.ui.theme.listItemColor

@Composable
fun ProfileMenuButton(
    text: String,
    style: TextStyle = TextStyle(),
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadowModifier(
                listItemColor(),
                onClick = { onClick() }),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, style = style)
        Icon(Icons.Filled.ArrowForward, contentDescription = text)
    }
}