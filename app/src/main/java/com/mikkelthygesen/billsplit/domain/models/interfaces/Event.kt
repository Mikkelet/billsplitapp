package com.mikkelthygesen.billsplit.domain.models.interfaces

import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.Person

interface Event {
    val id: String
    val createdBy: Person
    val timeStamp: Long

    fun toDTO(): EventDTO? {
        return when (this) {
            is GroupExpense -> EventDTO.fromExpense(this)
            is Payment -> EventDTO.fromPayment(this)
            is GroupExpensesChanged -> EventDTO.fromChange(this)
            else -> null
        }
    }
}