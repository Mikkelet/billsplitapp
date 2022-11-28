package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.UUID

data class Person(
    val uid: String = UUID.randomUUID().toString(),
    private var name: String = "",
    val pfpResId:Int = 0,
) {
    var nameState by mutableStateOf(name)

    override fun equals(other: Any?): Boolean {
        return if (other is Person)
            this.uid == other.uid
        else false
    }

    override fun hashCode(): Int {
        return uid.hashCode()
    }
}