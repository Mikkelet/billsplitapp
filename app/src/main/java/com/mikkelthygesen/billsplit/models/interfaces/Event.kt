package com.mikkelthygesen.billsplit.models.interfaces

import com.mikkelthygesen.billsplit.models.Person

interface Event {
    val id: String
    val createdBy: Person
    val timeStamp: Long
}