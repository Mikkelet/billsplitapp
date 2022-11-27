package com.mikkelthygesen.billsplit.models

data class Group(
    val id: String,
    val name: String,
    val people: List<Person>
)