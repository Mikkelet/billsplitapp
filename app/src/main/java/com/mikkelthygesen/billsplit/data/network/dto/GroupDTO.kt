package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.models.Group

@kotlinx.serialization.Serializable
data class GroupDTO(
    val id: String,
    val name: String,
    val people: List<PersonDTO>,
    val createdBy: PersonDTO,
    val timeStamp: Long
) {
    fun toGroup() = Group(
        id = id,
        name = name,
        createdBy = createdBy.toPerson(),
        timeStamp = timeStamp,
        events = emptyList(),
        people = people.map { it.toPerson() }
    )

    companion object {

        fun fromGroup(group: Group) = GroupDTO(
            id = group.id,
            name = group.nameState,
            people = group.peopleState.map { PersonDTO.fromPerson(it) },
            createdBy = PersonDTO.fromPerson(group.createdBy),
            timeStamp = group.timeStamp
        )
    }
}