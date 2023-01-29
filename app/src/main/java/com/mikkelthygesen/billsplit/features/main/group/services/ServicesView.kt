package com.mikkelthygesen.billsplit.features.main.group.services

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ServicesView() {
    Box {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Add a service"
        )
    }
}