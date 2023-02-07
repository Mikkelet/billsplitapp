package com.mikkelthygesen.billsplit.data.remote.dto

import com.mikkelthygesen.billsplit.data.local.database.model.GroupDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.DebtDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.Person

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
        createdBy = Person(createdBy),
        timeStamp = timeStamp,
        people = people.map { Person(it) },
        debts = debts.map { it.toDebt() }
    )

    fun toDB() = GroupDb(
        id = id,
        name = name,
        createdBy = PersonDb(createdBy),
        timestamp = timeStamp,
        debts = debts.map { DebtDb(it) },
        people = people.map { PersonDb(it) },
    )

    companion object {
        fun fromGroup(group: Group) = GroupDTO(
            id = group.id,
            name = group.nameState,
            people = group.peopleState.map { PersonDTO(it) },
            createdBy = PersonDTO(group.createdBy),
            timeStamp = group.timeStamp,
            debts = group.debtsState.map { DebtDTO(it.first, it.second) }
        )
    }
}