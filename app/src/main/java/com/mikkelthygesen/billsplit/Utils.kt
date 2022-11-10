package com.mikkelthygesen.billsplit

fun <T> tryCatchDefault(defaultValue: T, callback: () -> T) = try {
    callback()
} catch (e: Exception) {
    println(e)
    defaultValue
}

fun tryParseToFloat(person: Person, value: String) = try {
    person.owed = value.toFloat()
    false
} catch (e: Exception) {
    true
}