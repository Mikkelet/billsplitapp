package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorView(error: Throwable, onRetry: () -> Unit = {}) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$error")
            IconButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRetry) {
                Icon(painter = rememberVectorPainter(image = Icons.Filled.Refresh),
                    contentDescription = "Retry request")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(){
    ErrorView(error = Throwable("OH NO ERROR"))
}