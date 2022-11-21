package com.mikkelthygesen.billsplit

import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.Payment

class DebtCalculator(
    private val people: List<Person>,
    private val expenses: List<GroupExpense>,
    private val payments: List<Payment>
) {

    fun calculateDebtsByPayee(): List<Pair<Person, List<Pair<Person, Float>>>> =
        calculateDebts(people, expenses)

    fun calculatesDebtByIndebted(): List<Pair<Person, List<Pair<Person, Float>>>> =
        calculateDebtTo(people, expenses)

    fun calculateEffectiveDebtOfPerson(person: Person): List<Pair<Person, Float>> =
        calculateEffectiveDebt(person, people, expenses)

    fun calculateDebtsForPersonAfterPayments(person: Person): List<Pair<Person, Float>> =
        calculateDebtsAfterPayments(person, people, expenses, payments)

    fun logDebt(person: Person) {
        println("==== DEBT ====")
        calculateDebtsByPayee().forEach { pair ->
            val payee = pair.first
            val debts = pair.second
            println("${payee.nameState} is owed: ")
            debts.forEach {
                val ie = it.first
                val debt = it.second
                println("\t$$debt by ${ie.nameState}")
            }
        }
        println("\n=== IND DEBT ===")
        calculatesDebtByIndebted().forEach { pair ->
            val payee = pair.first
            val payeeDebts = pair.second
            println("${payee.nameState} owes")
            payeeDebts.forEach {
                val ie = it.first
                val debt = it.second
                println("\t$$debt to ${ie.nameState}")
            }
        }
        println("\n=== Effect Debt ===")
        println("${person.nameState} owes:")
        val debt = calculateEffectiveDebtOfPerson(person)
        debt.forEach {
            println("\tto ${it.first.nameState}: $${it.second}")
        }
    }
}