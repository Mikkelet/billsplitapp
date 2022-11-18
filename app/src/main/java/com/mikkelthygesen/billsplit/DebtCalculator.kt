package com.mikkelthygesen.billsplit

import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.GroupExpense

class DebtCalculator(
    private val people: List<Person>,
    private val expenses: List<GroupExpense>
) {

    fun calculateDebtsByPayee(): List<Pair<Person, List<Pair<Person, Float>>>> =
        calculateDebts(people, expenses)

    fun calculatesDebtByIndebted(): List<Pair<Person, List<Pair<Person, Float>>>> =
        calculateDebtTo(people, expenses)

    fun calculateEffectiveDebtOfPerson(person: Person): List<Pair<Person, Float>> =
        calculateEffectiveDebt(person, people, expenses)

    fun logDebt(person: Person) {
        println("==== DEBT ====")
        calculateDebtsByPayee().forEach { pair ->
            val payee = pair.first
            val debts = pair.second
            println("${payee.name} is owed: ")
            debts.forEach {
                val ie = it.first
                val debt = it.second
                println("\t$$debt by ${ie.name}")
            }
        }
        println("\n=== IND DEBT ===")
        calculatesDebtByIndebted().forEach { pair ->
            val payee = pair.first
            val payeeDebts = pair.second
            println("${payee.name} owes")
            payeeDebts.forEach {
                val ie = it.first
                val debt = it.second
                println("\t$$debt to ${ie.name}")
            }
        }
        println("\n=== Effect Debt ===")
        println("${person.name} owes:")
        val debt = calculateEffectiveDebtOfPerson(person)
        debt.forEach {
            println("\tto ${it.first.name}: $${it.second}")
        }
    }
}