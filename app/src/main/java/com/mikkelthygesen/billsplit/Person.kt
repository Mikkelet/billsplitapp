package com.mikkelthygesen.billsplit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Person(
    name: String,
    owed: Float,
    isParticipant: Boolean = true
) {
    var name by mutableStateOf(name)
    var owed by mutableStateOf(owed)
    var isParticipant by mutableStateOf(isParticipant)
}