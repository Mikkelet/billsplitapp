package com.mikkelthygesen.billsplit.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.data.remote.dto.PersonDTO

data class Person(
    val uid: String = "",
    private var name: String = "",
    private val pfpUrl: String = "",
    val email: String = ""
) {
    constructor(personDTO: PersonDTO):this(
        uid = personDTO.id,
        name = personDTO.name,
        pfpUrl = personDTO.pfpUrl
    )

    constructor(personDb: PersonDb) : this(
        uid = personDb.uid,
        name = personDb.name,
        pfpUrl = personDb.pfpUrl
    )

    var nameState by mutableStateOf(name.ifBlank { "Splitsby User" })
    var pfpUrlState by mutableStateOf(pfpUrl)

    fun isNameChanged(): Boolean {
        return nameState != name
    }

    fun saveChanges() {
        name = nameState
    }

    fun resetState() {
        nameState = name
    }

    override fun equals(other: Any?): Boolean {
        if (other is Person)
            return uid == other.uid
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return uid.hashCode()
    }

    fun toDb() = PersonDb(
        uid = uid,
        name = nameState,
        pfpUrl = pfpUrlState
    )
}