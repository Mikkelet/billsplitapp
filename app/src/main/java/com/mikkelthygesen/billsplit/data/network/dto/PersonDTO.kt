package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.data.local.room.models.PersonDb
import com.mikkelthygesen.billsplit.models.Person

@kotlinx.serialization.Serializable
data class PersonDTO(
    val id: String,
    val name: String,
    val pfpUrl: String
) {
    fun toPerson() = Person(
        uid = id,
        name = name,
        pfpUrl = pfpUrl
    )

    fun toDB() = PersonDb(
        uid = id,
        name = name,
        pfpUrl = pfpUrl
    )

    companion object {
        fun fromPerson(person: Person) = PersonDTO(
            id = person.uid,
            name = person.nameState,
            pfpUrl = ""
        )
    }
}
