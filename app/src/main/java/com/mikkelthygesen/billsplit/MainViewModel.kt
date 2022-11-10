package com.mikkelthygesen.billsplit

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer

class MainViewModel : ViewModel() {
    private val shared = Person("shared", 0F)
    private val peopleStateFlow = MutableStateFlow<List<Person>>(listOf(shared))
    private val sharedOwedFlow = MutableStateFlow(0F)
    private var peopleAdded = 0
    val peopleState: StateFlow<List<Person>> = peopleStateFlow
    val sharedOwedState: StateFlow<Float> = sharedOwedFlow

    fun addPerson() {
        peopleAdded++
        val person = Person("Person $peopleAdded", 0F)
        val updateList: List<Person> = peopleState.value + listOf(person)
        peopleStateFlow.value = updateList
        updateSharedOwed()
    }

    fun updateOwed(person: Person, owed: Float) {
        val index = peopleState.value.indexOf(person)
        val list = peopleState.value.map { Person(it.name, it.owed) }
        val tempPerson = list[index]
        tempPerson.owed = owed
        peopleStateFlow.value = list
        updateSharedOwed()
    }

    fun updateSharedExpenses(owed: Float) {
        val index = peopleState.value.indexOf(getSharedPerson())
        val list = peopleState.value.map { Person(it.name, it.owed) }
        val tempPerson = list[index]
        tempPerson.owed = owed
        peopleStateFlow.value = list
        updateSharedOwed()
    }

    fun removePerson(person: Person) {
        val indexOf = peopleState.value.indexOf(person)
        val updateList = peopleState.value.toMutableList()
        updateList.removeAt(indexOf)
        peopleStateFlow.value = updateList
        updateSharedOwed()
    }

    private fun updateSharedOwed() {
        val participants = peopleState.value.size - 1
        val sharedOwed = getSharedPerson().owed
        sharedOwedFlow.value = sharedOwed / participants
    }

    private fun getSharedPerson(): Person = peopleState.value.first()
}