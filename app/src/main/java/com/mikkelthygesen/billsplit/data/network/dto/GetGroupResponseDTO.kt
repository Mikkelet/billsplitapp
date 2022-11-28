package com.mikkelthygesen.billsplit.data.network.dto

@kotlinx.serialization.Serializable
data class GetGroupResponseDTO(
    val group: GroupDTO,
    val events: List<EventDTO>,
    val people: List<PersonDTO>
)
