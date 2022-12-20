package com.mikkelthygesen.billsplit.features.group.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.widgets.ProfilePicture

@Composable
fun ParticipantsView(list: List<Person>) {
    Row(Modifier.height(20.dp)) {
        list.map {
            Box(modifier = Modifier.padding(horizontal = 2.dp)) {
                ProfilePicture(
                    modifier = Modifier.width(20.dp),
                    person = it
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Box(modifier = Modifier.size(100.dp)) {
        val person = Person(name = "Mikkel")
        ParticipantsView(list = listOf(person, person, person))
    }
}