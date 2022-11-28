package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person

@kotlinx.serialization.Serializable
data class GetGroupResponseDTO(
    val group: GroupDTO,
    val events: List<EventDTO>,
    val people: List<PersonDTO>
) {

    fun toGroup() = Group(
        id = group.id,
        name = group.name,
        people = people.map { it.toPerson() },
        createdBy = Person(uid =  group.createdBy),
        timeStamp = group.timeStamp,
        events = events.map { it.toEvent() }
    )
}
