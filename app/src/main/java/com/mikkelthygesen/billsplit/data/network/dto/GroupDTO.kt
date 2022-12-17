package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.data.local.room.models.DebtDb
import com.mikkelthygesen.billsplit.data.local.room.models.GroupDb
import com.mikkelthygesen.billsplit.models.Group

@kotlinx.serialization.Serializable
data class GroupDTO(
    val id: String,
    val name: String,
    val people: List<PersonDTO>,
    val createdBy: PersonDTO,
    val timeStamp: Long,
    val debts: List<DebtDTO>
) {
    fun toGroup() = Group(
        id = id,
        name = name,
        createdBy = createdBy.toPerson(),
        timeStamp = timeStamp,
        events = emptyList(),
        people = people.map { it.toPerson() },
        debts = debts.map { it.toDebt() }
    )

    fun toDB() = GroupDb(
        id = id,
        name = name,
        timestamp = timeStamp,
        debts = debts.map { it.toDb() }
    )

    companion object {
        fun fromGroup(group: Group) = GroupDTO(
            id = group.id,
            name = group.nameState,
            people = group.peopleState.map { PersonDTO.fromPerson(it) },
            createdBy = PersonDTO.fromPerson(group.createdBy),
            timeStamp = group.timeStamp,
            debts = group.debtsState.map { DebtDTO(it.first, it.second) }
        )
    }
}