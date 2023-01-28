package com.mikkelthygesen.billsplit.features.main.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.widgets.ProfileHeader
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.ui.theme.listItemColor

@Composable
@SuppressLint("ComposableNaming")
fun ProfileView(
    user: Person,
    onUpdateUser: suspend () -> Unit,
    onError: (Throwable) -> Unit
) {
    val profileViewModel: ProfileViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(user, onUpdateUser, onError)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadowModifier(MaterialTheme.colors.listItemColor(),
                    onClick = {
                        profileViewModel.showFriends()

                    }),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Friends")
            Icon(Icons.Filled.ArrowForward, contentDescription = "Friens")
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    ProfileView(Person(name = "Catra"), onUpdateUser = {}, onError = {})
}