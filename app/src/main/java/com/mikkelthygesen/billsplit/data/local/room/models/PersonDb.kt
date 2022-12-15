package com.mikkelthygesen.billsplit.data.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikkelthygesen.billsplit.models.Person

@Entity(tableName = "people")
data class PersonDb(
    @PrimaryKey
    val uid: String,
    val name: String,
    val pfpUrl: String
) {
    fun toPerson() = Person(
        uid = uid,
        name = name,
        pfpUrl = pfpUrl,
        email = ""
    )
}