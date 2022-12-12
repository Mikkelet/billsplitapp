package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.R

data class Person(
    val uid: String = "",
    private var name: String = "",
    val pfpResId: Int = R.drawable.catra_pfp,
    private val pfpUrl: String = ""
) {
    var nameState by mutableStateOf(name)
    var pfpUrlState by mutableStateOf(pfpUrl)

    fun isNameChanged(): Boolean {
        return nameState != name
    }

    fun saveChanges() {
        name = nameState
    }

    override fun equals(other: Any?): Boolean {
        if (other is Person)
            return uid == other.uid
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return uid.hashCode()
    }
}