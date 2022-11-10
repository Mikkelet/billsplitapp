package com.mikkelthygesen.billsplit

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val shared = Person("shared", 0F)
    private val peopleStateFlow = MutableStateFlow<List<Person>>(listOf(shared))
    private var peopleAdded = 0
    val participantsState: StateFlow<List<Person>> = peopleStateFlow

    fun addPerson() {
        peopleAdded++
        val person = Person("Person $peopleAdded", 0F)
        val updateList: List<Person> = participantsState.value + listOf(person)
        peopleStateFlow.value = updateList
    }

    fun updateOwed(person: Person, owed: Float) {
        person.owed = owed
    }

    fun updateSharedExpenses(owed: Float) {
        shared.owed = owed
    }

    fun removePerson(person: Person) {
        val indexOf = participantsState.value.indexOf(person)
        val updateList = participantsState.value.toMutableList()
        updateList.removeAt(indexOf)
        peopleStateFlow.value = updateList
    }


    fun getSharedExpenses(): Person = participantsState.value.first()
}