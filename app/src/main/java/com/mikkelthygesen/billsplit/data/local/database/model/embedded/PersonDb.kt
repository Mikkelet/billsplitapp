package com.mikkelthygesen.billsplit.data.local.database.model.embedded

import com.mikkelthygesen.billsplit.data.remote.dto.PersonDTO
import com.mikkelthygesen.billsplit.domain.models.Person

@kotlinx.serialization.Serializable
data class PersonDb(
    val uid: String,
    val name: String,
    val pfpUrl: String
) {

    constructor(person: Person) : this(
        uid = person.uid,
        name = person.nameState,
        pfpUrl = person.pfpUrlState
    )

    constructor(person: PersonDTO) : this(
        uid = person.id,
        name = person.name,
        pfpUrl = person.pfpUrl
    )

    fun toPerson() = Person(
        uid = uid,
        name = name,
        pfpUrl = pfpUrl,
        email = ""
    )
}