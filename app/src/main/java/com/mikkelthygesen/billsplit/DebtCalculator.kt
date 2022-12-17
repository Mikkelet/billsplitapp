package com.mikkelthygesen.billsplit

import com.mikkelthygesen.billsplit.models.*

class DebtCalculator(
    private val people: List<Person>,
    expenses: List<GroupExpense>,
    payments: List<Payment>
) {
    private val groupExpenses = expenses.plus(payments.map { it.asExpense() })

    private fun calculateDebtsByPayee(): List<Pair<Person, List<Pair<Person, Float>>>> =
        calculateDebts(people, groupExpenses)

    private fun calculatesDebtByIndebted(): List<Pair<Person, List<Pair<Person, Float>>>> =
        calculateDebtTo(people, groupExpenses)

    fun calculateEffectiveDebtOfPerson(person: Person): List<Pair<Person, Float>> =
        calculateEffectiveDebt(person, people, groupExpenses)

    fun calculateEffectiveDebtForGroup(): List<Pair<String, Float>> {
        return people.map { person ->
            person.uid to calculateEffectiveDebtOfPerson(person)
                .map { it.second }
                .reduceOrZero()
        }
    }

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