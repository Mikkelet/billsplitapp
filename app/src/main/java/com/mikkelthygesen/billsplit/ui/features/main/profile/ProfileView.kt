package com.mikkelthygesen.billsplit.ui.features.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.ui.features.main.widgets.ProfileHeader
import com.mikkelthygesen.billsplit.ui.widgets.CircularImageView

@Composable
fun ProfileView(user: Person) {
    Column {
        ProfileHeader(user)
        FriendsView(friends = samplePeopleShera)
    }
}

@Composable
fun FriendsView(friends: List<Person>) {
    Column {
        Text(text = "Friends", style = TextStyle(fontSize = 30.sp))
        LazyColumn {
            items(friends.size) { index ->
                val friend = friends[index]
                Row {
                    CircularImageView(
                        modifier = Modifier.size(15.dp), imageResId = friend.pfpResId
                    )
                    Text(text = friend.nameState, style = TextStyle(fontSize = 15.sp))
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    ProfileView(Person())
}