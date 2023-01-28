package com.mikkelthygesen.billsplit.features.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme

@Composable
fun BaseScaffold(
    floatingActionButton: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    BillSplitTheme {
        Scaffold(
            backgroundColor = MaterialTheme.colors.background,
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
        ) {
            Box(
                Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                content()
            }
        }
    }
}