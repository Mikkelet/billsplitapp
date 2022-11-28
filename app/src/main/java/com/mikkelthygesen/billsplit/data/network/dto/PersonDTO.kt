package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.R
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
        pfpResId = R.drawable.catra_pfp
    )
}
