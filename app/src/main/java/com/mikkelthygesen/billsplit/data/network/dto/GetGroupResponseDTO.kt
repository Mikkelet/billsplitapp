package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.models.Group

@kotlinx.serialization.Serializable
data class GetGroupResponseDTO(
    val group: GroupDTO,
    val events: List<EventDTO>,
) {

    fun toGroup(): Group {
        return Group(
            id = group.id,
            name = group.name,
            people = group.people.map { it.toPerson() },
            createdBy = group.createdBy.toPerson(),
            timeStamp = group.timeStamp,
            events = events.map { it.toEvent() }
        )
    }
}
