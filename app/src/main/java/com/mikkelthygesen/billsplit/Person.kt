package com.mikkelthygesen.billsplit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

class Person(
    val name: String,
    owed: Float
) {
    val owed by mutableStateOf(owed)
}