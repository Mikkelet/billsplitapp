package com.mikkelthygesen.billsplit.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.data.local.database.model.*
import com.mikkelthygesen.billsplit.data.remote.dto.GroupDTO
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event

data class Group(
    val id: String,
    private var name: String = "",
    private var people: List<Person> = emptyList(),
    val createdBy: Person = Person(),
    val timeStamp: Long = System.currentTimeMillis(),
    val debts: List<Pair<String, Float>> = emptyList(),
    val latestEvent: Event? = null
) {
    constructor(groupDb: GroupDb, latestEvent: EventDb?) : this(
        id = groupDb.id,
        name = groupDb.name,
        timeStamp = groupDb.timestamp,
        people = groupDb.people.map { Person(it) },
        debts = groupDb.debts.map { it.toDebt() },
        latestEvent = when (latestEvent) {
            is GroupExpenseDb -> GroupExpense(latestEvent)
            is PaymentDb -> Payment(latestEvent)
            is ExpenseChangeDb -> GroupExpensesChanged(latestEvent)
            else -> null
        }
    )

    constructor(groupDTO: GroupDTO) : this(
        id = groupDTO.id,
        name = groupDTO.name,
        timeStamp = groupDTO.timeStamp,
        people = groupDTO.people.map { Person(it) },
        debts = groupDTO.debts.map { it.toDebt() },
        latestEvent = groupDTO.latestEvent?.toEvent()
    )

    var nameState by mutableStateOf(name)
    var peopleState by mutableStateOf(people)

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
}