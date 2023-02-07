package com.mikkelthygesen.billsplit.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.DebtDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupDb
import com.mikkelthygesen.billsplit.data.remote.dto.GroupDTO

data class Group(
    val id: String,
    private var name: String = "",
    private var people: List<Person> = emptyList(),
    val createdBy: Person = Person(),
    val timeStamp: Long = System.currentTimeMillis(),
    private var debts: List<Pair<String, Float>> = emptyList(),
) {
    constructor(groupDb: GroupDb) : this(
        id = groupDb.id,
        name = groupDb.name,
        timeStamp = groupDb.timestamp,
        people = groupDb.people.map { Person(it) },
        debts = groupDb.debts.map { it.toDebt() }
    )

    constructor(groupDTO: GroupDTO) : this(
        id = groupDTO.id,
        name = groupDTO.name,
        timeStamp = groupDTO.timeStamp,
        people = groupDTO.people.map { Person(it) },
        debts = groupDTO.debts.map { it.toDebt() }
    )

    var nameState by mutableStateOf(name)
    var peopleState by mutableStateOf(people)
    var debtsState by mutableStateOf(debts)

    fun updateGroup(group: Group) {
        nameState = group.nameState
        peopleState = group.peopleState
    }

    fun addPerson(person: Person) {
        peopleState = peopleState.plus(person)
    }

    fun removePerson(person: Person) {
        peopleState = peopleState.minus(person)
    }

    fun applyChanges() {
        name = nameState
        people = listOf(*peopleState.toTypedArray())
    }

    fun toDb() = GroupDb(
        id = id,
        name = nameState,
        createdBy = createdBy.toDb(),
        timestamp = timeStamp,
        people = people.map { it.toDb() },
        debts = debts.map { DebtDb(it.first, it.second) }
    )
}