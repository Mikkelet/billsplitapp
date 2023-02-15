package com.mikkelthygesen.billsplit.features.main.widgets.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.ui.shadowModifier
import java.lang.Exception

@Composable
fun DialogHandler(viewModel: BaseViewModel){
    when (val dialogState = viewModel.dialogState) {
        is BaseViewModel.DialogState.Error ->
            ErrorDialog(
                exception = dialogState.exception,
                onDismiss = viewModel::dismissDialog
            )
    }
}

@Composable
fun ErrorDialog(
    exception: Throwable,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.shadowModifier(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "An error occurred",
                style = MaterialTheme.typography.titleSmall
                    .copy(color = MaterialTheme.colorScheme.onBackground)
            )
            Text(
                text = let {
                    if(BuildConfig.DEBUG)
                        "(${exception.javaClass.simpleName}) ${exception.message}"
                    else "${exception.message}"
                },
                style = MaterialTheme.typography.bodyMedium
                    .copy(color = MaterialTheme.colorScheme.error)
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