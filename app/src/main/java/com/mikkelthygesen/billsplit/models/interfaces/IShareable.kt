package com.mikkelthygesen.billsplit.models.interfaces

import com.mikkelthygesen.billsplit.models.Person

interface IShareable {
    val createdBy: Person
    val timeStamp: Long
}