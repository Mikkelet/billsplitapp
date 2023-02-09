package com.mikkelthygesen.billsplit.data.remote.dto

import com.mikkelthygesen.billsplit.domain.models.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.*

@kotlinx.serialization.Serializable
sealed class EventDTO {

    @kotlinx.serialization.Serializable
    @SerialName(TYPE_EXPENSE)
    data class ExpenseDTO(
        val id: String,
        val createdBy: PersonDTO,
        val timeStamp: Long,
        val description: String,
        val payee: PersonDTO,
        val sharedExpense: Float,
        val individualExpenses: List<IndividualExpenseDTO>,
    ) : EventDTO()

    @kotlinx.serialization.Serializable
    @SerialName(TYPE_PAYMENT)
    data class PaymentDTO(
        val id: String,
        val createdBy: PersonDTO,
        val timeStamp: Long,
        val paidTo: PersonDTO,
        val amount: Float,
    ) : EventDTO()

    @kotlinx.serialization.Serializable
    @SerialName(TYPE_CHANGE)
    data class ChangeDTO(
        val id: String,
        val createdBy: PersonDTO,
        val timeStamp: Long,
        val groupExpenseOriginal: EventDTO,
        val groupExpenseEdited: EventDTO
    ) : EventDTO()

    companion object {
        const val TYPE_PAYMENT = "payment"
        const val TYPE_CHANGE = "change"
        const val TYPE_EXPENSE = "expense"

        fun fromExpense(expense: GroupExpense): ExpenseDTO = ExpenseDTO(
            id = expense.id,
            description = expense.descriptionState,
            createdBy = PersonDTO(expense.createdBy),
            sharedExpense = expense.sharedExpenseState,
            individualExpenses = expense.individualExpenses.map {
                IndividualExpenseDTO(it)
            },
            payee = PersonDTO(expense.payeeState),
            timeStamp = expense.timeStamp,
        )

        fun fromPayment(payment: Payment): PaymentDTO = PaymentDTO(
            id = payment.id,
            timeStamp = payment.timeStamp,
            createdBy = PersonDTO(payment.createdBy),
            amount = payment.amount,
            paidTo = PersonDTO(payment.paidTo)
        )

        fun fromChange(expensesChanged: GroupExpensesChanged): ChangeDTO = ChangeDTO(
            id = expensesChanged.id,
            timeStamp = expensesChanged.timeStamp,
            createdBy = PersonDTO(expensesChanged.createdBy),
            groupExpenseEdited = fromExpense(expensesChanged.groupExpenseEdited),
            groupExpenseOriginal = fromExpense(expensesChanged.groupExpenseOriginal)
        )
    }

    fun toEvent() = when (this) {
        is ChangeDTO -> GroupExpensesChanged(
            id = id,
            createdBy = Person(createdBy),
            timeStamp = timeStamp,
            groupExpenseOriginal = let {
                val expense = groupExpenseOriginal as ExpenseDTO
                GroupExpense(
                    id = expense.id,
                    timeStamp = expense.timeStamp,
                    createdBy = Person(expense.createdBy),
                    description = expense.description,
                    sharedExpense = expense.sharedExpense,
                    payee = Person(expense.payee),
                    individualExpenses = expense.individualExpenses.map { IndividualExpense(it) }
                )
            },
            groupExpenseEdited = let {
                val expense = groupExpenseEdited as ExpenseDTO
                GroupExpense(
                    id = expense.id,
                    timeStamp = expense.timeStamp,
                    createdBy = Person(expense.createdBy),
                    description = expense.description,
                    sharedExpense = expense.sharedExpense,
                    payee = Person(expense.payee),
                    individualExpenses = groupExpenseEdited.individualExpenses.map { IndividualExpense(it) }
                )
            }
        )
        is PaymentDTO -> Payment(
            id = id,
            createdBy = Person(createdBy),
            timeStamp = timeStamp,
            paidTo = Person(paidTo),
            amount = amount
        )
        is ExpenseDTO -> GroupExpense(
            id = id,
            timeStamp = timeStamp,
            createdBy = Person(createdBy),
            payee = Person(payee),
            individualExpenses = individualExpenses.map { IndividualExpense(it) },
            sharedExpense = sharedExpense,
            description = description
        )
        else -> throw Exception("Unknown type")
    }
}
