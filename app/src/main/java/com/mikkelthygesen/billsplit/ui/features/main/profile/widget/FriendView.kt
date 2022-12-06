package com.mikkelthygesen.billsplit.ui.features.main.profile.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.widgets.CircularImageView


@Composable
fun FriendRequestSent(person: Person) {
    _FriendView(person = person) {
        Text(
            text = "Request sent",
            style = MaterialTheme.typography.subtitle1.copy(color = Color.Gray)
        )
    }
}

@Composable
fun AcceptFriend(
    mainViewModel: MainViewModel = viewModel(),
    person: Person
) {
    _FriendView(person = person) {
        Button(onClick = {
            mainViewModel.acceptFriendRequest(person.uid)
        }) {
            Text(text = "Accept")
        }
    }
}

@Composable
fun FriendView(person: Person) {
    _FriendView(person = person)
}


@Composable
@SuppressLint("ComposableNaming")
private fun _FriendView(
    person: Person,
    trailingView: @Composable () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularImageView(
                modifier = Modifier.size(64.dp),
                imageResId = person.pfpResId
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = person.nameState,
                style = MaterialTheme.typography.subtitle2
            )
        }
        trailingView()
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFriendRequestSent() {
    FriendView(person = samplePeopleShera.first())
}

@Preview(showBackground = true)
@Composable
private fun PreviewAcceptRequest() {
    FriendRequestSent(person = samplePeopleShera.first())
}

@Preview(showBackground = true)
@Composable
private fun PreviewFriend() {
    AcceptFriend(person = samplePeopleShera.first())
}