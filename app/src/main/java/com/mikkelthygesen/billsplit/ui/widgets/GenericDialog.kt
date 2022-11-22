package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun GenericDialog(
    dialogText: String,
    primaryText: String,
    primaryAction: () -> Unit,
    secondaryText: String,
    secondaryAction: () -> Unit
) {
    Dialog(onDismissRequest = primaryAction) {
        Card(
            modifier = Modifier
        ) {
            Column(
                Modifier.padding(16.dp),
                Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 12.dp),
                    text = dialogText,
                    style = TextStyle(fontSize = 20.sp)
                )
                Button(onClick = primaryAction) {
                    Text(text = primaryText)
                }
                Button(
                    onClick = secondaryAction,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colors.primary,
                        backgroundColor = MaterialTheme.colors.background
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Text(text = secondaryText)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewGenericDialog() {
    GenericDialog(
        dialogText = "Are you sure you want to do the thing?",
        primaryText = "Yes!!",
        primaryAction = {},
        secondaryText = "I'd rather be dragged to hell!",
        secondaryAction = {}
    )
}