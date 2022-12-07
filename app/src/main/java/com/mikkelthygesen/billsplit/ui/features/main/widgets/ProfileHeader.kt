package com.mikkelthygesen.billsplit.ui.features.main.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.widgets.CircularImageView


@Composable
fun ProfileHeader(
    user: Person
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularImageView(
            modifier = Modifier
                .size(100.dp),
            imageResId = user.pfpResId
        )
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp, bottom = 4.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
                .padding(16.dp),
            text = user.nameState,
            style = TextStyle(fontSize = 15.sp)
        )
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
                .padding(16.dp),
            text = "Test3@test.dk",
            style = TextStyle(fontSize = 15.sp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ProfileHeader(user = Person(name = "Catra"))
}