package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.UUID

data class Group(
    val id: String = UUID.randomUUID().toString(),
    private var name: String = "",
    private var people: List<Person> = emptyList()
){
    var nameState by mutableStateOf(name)
    var peopleState by mutableStateOf(people)

    fun addPerson(person: Person){
        peopleState = peopleState.plus(person)
    }

    fun applyChanges(){
        name = nameState
        people = listOf(*peopleState.toTypedArray())
    }
}