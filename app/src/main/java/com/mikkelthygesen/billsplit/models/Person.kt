package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Person(
    val pId: String,
    name: String
) {
    var name by mutableStateOf(name)

    override fun equals(other: Any?): Boolean {
        return if (other is Person)
            this.pId == other.pId
        else false
    }

    override fun hashCode(): Int {
        return pId.hashCode()
    }
}