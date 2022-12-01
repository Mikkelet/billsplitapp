package com.mikkelthygesen.billsplit.ui.features.group.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.widgets.CircularImageView

@Composable
fun ParticipantsView(list: List<Person>) {
    Row(Modifier.height(20.dp)) {
        list.map {
            CircularImageView(
                imageResId = it.pfpResId,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val person = Person(name = "Mikkel", pfpResId = R.drawable.shera_pfp)
    ParticipantsView(list = listOf(person, person, person))
}