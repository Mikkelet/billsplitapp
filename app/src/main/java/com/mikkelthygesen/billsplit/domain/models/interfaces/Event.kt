package com.mikkelthygesen.billsplit.domain.models.interfaces

import com.mikkelthygesen.billsplit.domain.models.Person

interface Event {
    val id: String
    val createdBy: Person
    val timeStamp: Long
}