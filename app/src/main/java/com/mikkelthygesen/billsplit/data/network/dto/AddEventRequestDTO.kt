package com.mikkelthygesen.billsplit.data.network.dto

@kotlinx.serialization.Serializable
data class AddEventRequestDTO(
    val groupId: String,
    val event: EventDTO
)