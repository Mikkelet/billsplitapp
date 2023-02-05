package com.mikkelthygesen.billsplit.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.DebtDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupDb
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event

data class Group(
    val id: String,
    private var name: String = "",
    private var people: List<Person> = emptyList(),
    val createdBy: Person = Person(),
    val timeStamp: Long = System.currentTimeMillis(),
    val events: List<Event> = emptyList(),
    val services: List<SubscriptionService> = emptyList(),
    private var debts: List<Pair<String, Float>>,
) {
    var nameState by mutableStateOf(name)
    var peopleState by mutableStateOf(people)
    var debtsState by mutableStateOf(debts)

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