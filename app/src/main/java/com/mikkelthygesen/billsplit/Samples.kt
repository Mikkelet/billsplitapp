package com.mikkelthygesen.billsplit

import com.mikkelthygesen.billsplit.models.ExpenseHolder.IndividualExpenseHolder
import com.mikkelthygesen.billsplit.models.ExpenseHolder.SharedExpenseHolder
import com.mikkelthygesen.billsplit.ui.features.shared_budget.GroupExpense

fun samplePeople() =
    (1..3).map {
        IndividualExpenseHolder("Person $it", it * 100F, true)
    }

fun sampleSharedExpense() = SharedExpenseHolder(samplePeople().size * 200F)

fun sampleSharedExpenses(): List<GroupExpense> {
    val people = samplePeople()
    val shared = sampleSharedExpense()
    return listOf(
        GroupExpense(
            "0",
            "",
            people[0],
            shared,
            people
        ),
        GroupExpense(
            "1",
            "",
            people[1],
            shared,
            people
        ),
        GroupExpense(
            "2",
            "",
            people[2],
            shared,
            people
        ),
        GroupExpense(
            "3",
            "",
            people[2],
            shared,
            people
        ),
        GroupExpense(
            "4",
            "",
            people[2],
            shared,
            people
        ),
    )
}

fun calculateDebts(
    people: List<IndividualExpenseHolder>,
    groupExpenses: List<GroupExpense>
): List<Pair<IndividualExpenseHolder, List<Pair<IndividualExpenseHolder, Float>>>> {
    return people.map { payee ->
        // get expenses payed for by payee
        val payedForGroupExpenses = groupExpenses.filter { ge -> ge.payee.name == payee.name }
        // Payee cannot have debt to themselves
        val payedForExpensesWithoutPayee = payedForGroupExpenses.map { ge -> ge.filterPayee() }
        // Update individual expenses to include the shared expense
        val payedForIndividualExpenses =
            payedForExpensesWithoutPayee.flatMap { it.getIndividualExpensesWithShared() }
        // get list of distinct indebted
        val distinctByName = payedForIndividualExpenses.distinctBy { it.name }
        val accExpensesByIe = distinctByName.map { indebted ->
            // for each distinct indebted, filter a list of their individual debts
            val debtsByIndebted = payedForIndividualExpenses.filter { it.name == indebted.name }
            // accumulate all their debts
            val totalDebt = debtsByIndebted.map { it.expense }.reduce { acc, fl -> acc + fl }
            Pair(indebted, totalDebt)
        }
        Pair(payee, accExpensesByIe)
    }
}

fun calculateDebtTo(
    people: List<IndividualExpenseHolder>,
    groupExpenses: List<GroupExpense>
): List<Pair<IndividualExpenseHolder, List<Pair<IndividualExpenseHolder, Float>>>> {
    val allDebtsByPayee = calculateDebts(people, groupExpenses)
    return allDebtsByPayee.map { debtsByPayee ->
        val payee = debtsByPayee.first
        val owedByPayee = allDebtsByPayee
            // filter expenses not paid by payee
            .filter { it.first.name != payee.name }
            // map expenses to list of indebted and their debt
            .map { debts ->
                val debtee = debts.first
                val debtToPayee = debts.second
                    .filter { it.first.name == payee.name }
                    .map { it.second }
                    .reduce { acc, fl -> acc + fl }
                Pair(debtee, debtToPayee)
            }
        Pair(payee, owedByPayee)
    }
}

fun calculateTotalDebt() {
    val total = sampleSharedExpenses()
        .map { it.filterPayee() }
        .flatMap {
            it.getIndividualExpensesWithShared()
        }.map {
            it.expense
        }.reduce { acc, fl -> acc + fl }
    println("Total=$total")
}

fun main() {
    calculateTotalDebt()
    println("==== DEBT ====")
    calculateDebts(samplePeople(), sampleSharedExpenses()).forEach { pair ->
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
    calculateDebtTo(samplePeople(), sampleSharedExpenses()).forEach { pair ->
        val payee = pair.first
        val payeeDebts = pair.second
        println("${payee.name} owes")
        payeeDebts.forEach {
            val ie = it.first
            val debt = it.second
            println("\t$$debt to ${ie.name}")
        }
    }
}