package com.mikkelthygesen.billsplit.data.network.dto

@kotlinx.serialization.Serializable
data class PersonDTO(
    val id: String,
    val name: String,
    val pfpUrl: String
)
