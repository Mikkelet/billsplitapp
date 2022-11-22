package com.mikkelthygesen.billsplit

import com.mikkelthygesen.billsplit.models.*
import java.util.*

class DebtCalculator(
    private val people: List<Person>,
    expenses: List<GroupExpense>,
    private val payments: List<Payment>
) {
    private val groupExpenses = expenses.plus(payments.map {
        GroupExpense(
            id = UUID.randomUUID().toString(),
            people.first(),
            description = "",
            it.createdBy,
            0F,
            listOf(
                IndividualExpense(it.paidTo, it.amount, true)
            )
        )
    })

    fun calculateDebtsByPayee(): List<Pair<Person, List<Pair<Person, Float>>>> =
        calculateDebts(people, groupExpenses)

    fun calculatesDebtByIndebted(): List<Pair<Person, List<Pair<Person, Float>>>> =
        calculateDebtTo(people, groupExpenses)

    fun calculateEffectiveDebtOfPerson(person: Person): List<Pair<Person, Float>> =
        calculateEffectiveDebt(person, people, groupExpenses)

    fun calculateDebtsForPersonAfterPayments(person: Person): List<Pair<Person, Float>> =
        calculateDebtsAfterPayments(person, people, groupExpenses, payments)

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