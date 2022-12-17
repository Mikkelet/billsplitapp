package com.mikkelthygesen.billsplit

import com.mikkelthygesen.billsplit.models.*
import kotlin.math.absoluteValue

val samplePeopleShera = listOf(
    Person("0", "Aang"),
    Person("1", "Toph"),
    Person("2", "Katara"),
)

val sampleGroup = Group(
    id = "GROUP0",
    name = "My group",
    people = samplePeopleShera,
    createdBy = samplePeopleShera.first(),
    timeStamp = 0,
    events = listOf()
)

val sampleIndividualExpenses = samplePeopleShera.mapIndexed { i, p ->
    IndividualExpense(p, i * 100F, true)
}

val sampleSharedExpense = sampleIndividualExpenses.size * 200F

val sampleSharedExpenses: List<GroupExpense>
    get() {
        val shared = sampleSharedExpense
        return listOf(
            GroupExpense(
                "0",
                samplePeopleShera[2],
                "Taking down the fire nation",
                samplePeopleShera[0],
                shared,
                sampleIndividualExpenses.map { it.copy() },
                1
            ),
            GroupExpense(
                "1",
                samplePeopleShera[2],
                "Beach day",
                samplePeopleShera[1],
                shared,
                sampleIndividualExpenses.map { it.copy() },
                2
            ),
            GroupExpense(
                "2",
                samplePeopleShera[1],
                "Appa haircut",
                samplePeopleShera[2],
                shared,
                sampleIndividualExpenses.map { it.copy() },
                3
            ),
            GroupExpense(
                "3",
                samplePeopleShera.first(),
                "",
                samplePeopleShera[2],
                shared,
                sampleIndividualExpenses.map { it.copy() },
                4
            ),
            GroupExpense(
                "4",
                samplePeopleShera.first(),
                "Foods",
                samplePeopleShera[2],
                shared,
                sampleIndividualExpenses.map { it.copy() },
                5
            ),
        )
    }

val samplePayments: List<Payment>
    get() {
        val person1 = samplePeopleShera[0]
        val person2 = samplePeopleShera[1]
        val person3 = samplePeopleShera[2]

        return listOf(
            Payment(
                createdBy = person2,
                paidTo = person3,
                amount = 500F,
                timeStamp = 6
            ),
            Payment(
                createdBy = person1,
                paidTo = person3,
                amount = 200F,
                timeStamp = 7
            ),
            Payment(
                createdBy = person2,
                paidTo = person1,
                amount = 100F,
                timeStamp = 8
            ),
        )
    }

fun calculateDebts(
    people: List<Person>,
    groupExpenses: List<GroupExpense>
): List<Pair<Person, List<Pair<Person, Float>>>> {
    return people.map { person ->
        // get expenses payed for by person
        val payedForGroupExpenses = groupExpenses.filter { ge -> ge.payeeState == person }
        // person cannot have debt to themselves
        val payedForExpensesWithoutPayee =
            payedForGroupExpenses.filter { it.payeeState == person }
        // Update individual expenses to include the shared expense
        val payedForIndividualExpenses =
            payedForExpensesWithoutPayee.flatMap { it.getIndividualExpensesWithShared() }
        // get list of distinct indebted
        val distinctById = payedForIndividualExpenses.distinctBy { it.person.uid }
        val accExpensesByIe = distinctById.map { indebted ->
            // for each distinct indebted, filter a list of their individual debts
            val debtsByIndebted =
                payedForIndividualExpenses.filter { it.person == indebted.person }
            // accumulate all their debts
            val totalDebt = debtsByIndebted.map { it.expenseState }.reduceOrZero()
            Pair(indebted.person, totalDebt)
        }
        Pair(person, accExpensesByIe)
    }
}

fun calculateDebtTo(
    people: List<Person>,
    groupExpenses: List<GroupExpense>
): List<Pair<Person, List<Pair<Person, Float>>>> {
    val allDebtsByPayee = calculateDebts(people, groupExpenses)
    return allDebtsByPayee.map { debtsByPayee ->
        val payee = debtsByPayee.first
        val owedByPayee = allDebtsByPayee
            // filter expenses not paid by payee, as payee cannot have debt to themselves
            .filter { it.first != payee }
            // Sort expenses into list of Pair(Person in debt, their debt)
            .map { debts ->
                val indebted = debts.first
                // Go through all expenses, filter those owed by indebted
                val debtToPayee = debts.second
                    .filter { it.first == payee }
                    .map { it.second }
                    .reduceOrZero()
                Pair(indebted, debtToPayee)
            }
        Pair(payee, owedByPayee)
    }
}

fun calculateEffectiveDebt(
    person: Person,
    people: List<Person>,
    groupExpenses: List<GroupExpense>
): List<Pair<Person, Float>> {
    val allDebt = calculateDebtTo(people, groupExpenses)
    val payeeDebt = calculateDebtTo(people, groupExpenses).first { it.first == person }
    val otherPayees = allDebt.filter { it.first != person }
    return otherPayees.map { otherPayee ->
        // filter payee debts to otherPayee and accumulate
        val otherPayeeDebtToPayee = otherPayee.second
            .filter { it.first == person }
            .map { it.second }
            .reduceOrZero()
        // filter otherPayee debt to payee and accumulate
        val payeeDebtToOtherPayee = payeeDebt.second
            .filter { it.first == otherPayee.first }
            .map { it.second }
            .reduceOrZero()
        val effectDebt = payeeDebtToOtherPayee - otherPayeeDebtToPayee
        Pair(otherPayee.first, effectDebt)
    }
}

fun calculateDebtsAfterPayments(
    person: Person,
    people: List<Person>,
    groupExpenses: List<GroupExpense>,
    payments: List<Payment>
): List<Pair<Person, Float>> {
    // get debts owed by person
    val effectiveDebt = calculateEffectiveDebt(person, people, groupExpenses)
    // for each debts, calculate the payment to negate potential debt
    return effectiveDebt.map { debt ->
        val debtee = debt.first
        val debtAmount = debt.second
        if (debtAmount > 0F) {
            // if debt exists, find payments paid by person to debtee
            val paymentsByPerson =
                payments.filter { it.createdBy == person && it.paidTo == debtee }
            val accPayments = paymentsByPerson.map { it.amount }.reduceOrZero()
            println("- ${person.nameState} owes $$debtAmount to ${debtee.nameState}, and have paid $accPayments ")
            Pair(debtee, debtAmount - accPayments)
        } else if (debtAmount < 0F) {
            // if debt is owed TO person (negative debt), find payments made by debtee to person
            val paymentsToPerson =
                payments.filter { it.paidTo == person && it.createdBy == debtee }
            val accPayments = paymentsToPerson.map { it.amount }.reduceOrZero()
            println("- ${debtee.nameState} owes $${debtAmount.absoluteValue} to ${person.nameState}, and have paid $accPayments ")
            Pair(debtee, debtAmount + accPayments)
        } else Pair(debtee, debtAmount) // Else just return the debts (should be 0F)
    }
}

fun calculateTotalDebt() {
    val total = sampleSharedExpenses
        .map { it.total }
        .reduceOrZero()
    println("Total=$total")
}

fun main() {
    calculateTotalDebt()
    println("==== DEBT ====")
    val people = sampleIndividualExpenses.map { it.person }
    val groupExpenses = sampleSharedExpenses
    calculateDebts(people, groupExpenses).forEach { pair ->
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
    calculateDebtTo(people, groupExpenses).forEach { pair ->
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
    sampleIndividualExpenses.forEach { ie ->
        println("${ie.person.nameState} owes:")
        val person = ie.person
        val debt = calculateEffectiveDebt(person, people, sampleSharedExpenses)
        debt.forEach {
            println("\tto ${it.first.nameState}: $${it.second}")
        }
    }
    println("\n=== After Payments ===")
    println("")
    samplePayments.forEach {
        println("${it.createdBy.nameState} paid $${it.amount} to ${it.paidTo.nameState}")
    }
    println("")
    samplePeopleShera.forEach { person ->
        println("Debts for ${person.nameState}")
        calculateDebtsAfterPayments(
            person,
            people = samplePeopleShera,
            groupExpenses = sampleSharedExpenses,
            payments = samplePayments,
        ).forEach {
            val otherPerson = it.first
            val debt = it.second
            if (debt > 0F)
                println("\t${otherPerson.nameState} owes $$debt to ${person.nameState}")
            else if (debt < 0F)
                println("\t${person.nameState} owes $$debt to ${otherPerson.nameState}")
        }
    }
}