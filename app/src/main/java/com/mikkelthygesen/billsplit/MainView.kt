package com.mikkelthygesen.billsplit

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainView(mainViewModel: MainViewModel = viewModel()){
    Box {
       Button(onClick = {
           mainViewModel.showGroup("")
       }) {
           Text(text = "Go to group")
       }
    }
}