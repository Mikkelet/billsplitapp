package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikkelthygesen.billsplit.models.Person

@Composable
fun ProfilePicture(modifier: Modifier = Modifier, person: Person) {
    if (person.pfpUrlState.isBlank())
        AutoProfilePic(modifier = modifier, user = person)
    else CircularUrlImageView(modifier, imageUrl = person.pfpUrlState)
}