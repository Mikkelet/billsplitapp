package com.mikkelthygesen.billsplit.data.local.database.model.embedded

import com.mikkelthygesen.billsplit.domain.models.Person

@kotlinx.serialization.Serializable
data class PersonDb(
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