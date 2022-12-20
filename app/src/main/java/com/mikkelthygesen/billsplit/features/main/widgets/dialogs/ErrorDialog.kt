package com.mikkelthygesen.billsplit.features.main.widgets.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mikkelthygesen.billsplit.features.main.profile.widget.shadowModifier
import java.lang.Exception

@Composable
fun <T : Throwable> ErrorDialog(
    exception: T,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.shadowModifier(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "An error occurred",
                style = MaterialTheme.typography.h6
                    .copy(color = MaterialTheme.colors.onBackground)
            )
            Text(
                text = "(${exception.javaClass.simpleName}) ${exception.message}",
                style = MaterialTheme.typography.body1
                    .copy(color = MaterialTheme.colors.error)
            )
            Button(
                modifier = Modifier.padding(top = 32.dp),
                onClick = onDismiss
            ) {
                Text(text = "OK")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ErrorDialog(exception = Exception("An error occurred!"), onDismiss = {})
}