package com.mikkelthygesen.billsplit.data.remote.dto

import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.Person

@kotlinx.serialization.Serializable
data class GroupDTO(
    val id: String,
    val name: String,
    val people: List<PersonDTO>,
    val createdBy: PersonDTO,
    val timeStamp: Long,
    val debts: List<DebtDTO>,
    val latestEvent: EventDTO?,
) {

    constructor(group: Group) : this(
        id = group.id,
        name = group.nameState,
        people = group.peopleState.map { PersonDTO(it) },
        createdBy = PersonDTO(group.createdBy),
        timeStamp = group.timeStamp,
        debts = group.debts.map { DebtDTO(it.first, it.second) },
        latestEvent = group.latestEvent?.toDTO()
    )

    fun toGroup() = Group(
        id = id,
        name = name,
        createdBy = Person(createdBy),
        timeStamp = timeStamp,
        people = people.map { Person(it) },
        debts = debts.map { it.toDebt() },
        latestEvent = latestEvent?.toEvent()
    )
}