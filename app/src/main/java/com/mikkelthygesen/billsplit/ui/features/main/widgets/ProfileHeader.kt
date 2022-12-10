package com.mikkelthygesen.billsplit.ui.features.main.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
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
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.shadowModifier
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
                .padding(top = 32.dp)
                .size(100.dp),
            imageResId = user.pfpResId
        )
        Column(
            shadowModifier(MaterialTheme.colors.background)
        ) {
            Text(
                text = user.nameState,
                style = TextStyle(fontSize = 32.sp)
            )
            Divider(Modifier.padding(vertical = 16.dp, horizontal = 64.dp))
            Text(
                text = "Test3@test.dk",
                style = TextStyle(fontSize = 20.sp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    ProfileHeader(user = Person("pokaspd123123psodak", name = "Catra"))
}