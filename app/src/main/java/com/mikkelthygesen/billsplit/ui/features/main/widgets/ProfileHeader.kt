package com.mikkelthygesen.billsplit.ui.features.main.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.widgets.CircularImageView


@Composable
fun ProfileHeader(){
    val user = MainViewModel.getLoggedIn()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        CircularImageView(
            modifier = Modifier
                .padding(16.dp)
                .size(64.dp),
            imageResId = user.pfpResId
        )
        Column(Modifier.padding(vertical = 16.dp)) {
            Text(text = user.nameState,
                style = TextStyle(fontSize = 30.sp)
            )
        }
    }
}