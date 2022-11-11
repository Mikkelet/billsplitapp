package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.Person

class SharedExpense(
    val id: String,
    description: String,
    val sharedExpense: Person,
    val people: List<Person>
) {
    var description by mutableStateOf(description)

    var participants by mutableStateOf(people.count { it.isParticipant })

    fun getTotal(): Float = people
        .map { it.owed }
        .reduce { acc, fl -> acc + fl }
}