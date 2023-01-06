package com.mikkelthygesen.billsplit.data.remote.dto

import com.mikkelthygesen.billsplit.data.local.database.model.ExpenseChangeDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupExpenseDb
import com.mikkelthygesen.billsplit.data.local.database.model.PaymentDb
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.Person
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
    ) : EventDTO() {

        fun toDb(groupId: String) = GroupExpenseDb(
            groupId = groupId,
            id = id,
            createdBy = createdBy.toDB(),
            description = description,
            payee = payee.toDB(),
            sharedExpense = sharedExpense,
            individualExpenses = individualExpenses.map { it.toDb()  },
            timeStamp = timeStamp
        )
    }

    @kotlinx.serialization.Serializable
    @SerialName(TYPE_PAYMENT)
    data class PaymentDTO(
        val id: String,
        val createdBy: PersonDTO,
        val timeStamp: Long,
        val paidTo: PersonDTO,
        val amount: Float,
    ) : EventDTO() {

        fun toDb(groupId: String) = PaymentDb(
            groupId = groupId,
            id = id,
            createdBy = createdBy.toDB(),
            timeStamp = timeStamp,
            paidTo = paidTo.toDB(),
            amount = amount,
        )
    }

    @kotlinx.serialization.Serializable
    @SerialName(TYPE_CHANGE)
    data class ChangeDTO(
        val id: String,
        val createdBy: PersonDTO,
        val timeStamp: Long,
        val groupExpenseOriginal: EventDTO,
        val groupExpenseEdited: EventDTO
    ) : EventDTO() {

        fun toDb(groupId: String) = ExpenseChangeDb(
            groupId = groupId,
            id = id,
            timeStamp = timeStamp,
            createdBy = createdBy.toDB(),
            groupExpenseOriginal = (groupExpenseOriginal as ExpenseDTO).toDb(groupId),
            groupExpenseEdited = (groupExpenseEdited as ExpenseDTO).toDb(groupId),
        )
    }

    companion object {
        const val TYPE_PAYMENT = "payment"
        const val TYPE_CHANGE = "change"
        const val TYPE_EXPENSE = "expense"

        private fun Person.toPersonDTO() = PersonDTO.fromPerson(this)

        fun fromExpense(expense: GroupExpense): ExpenseDTO = ExpenseDTO(
            id = expense.id,
            description = expense.descriptionState,
            createdBy = expense.createdBy.toPersonDTO(),
            sharedExpense = expense.sharedExpenseState,
            individualExpenses = expense.individualExpenses.map {
                IndividualExpenseDTO.fromIndividualExpense(it)
            },
            payee = expense.payeeState.toPersonDTO(),
            timeStamp = expense.timeStamp,
        )

        fun fromPayment(payment: Payment): PaymentDTO = PaymentDTO(
            id = payment.id,
            timeStamp = payment.timeStamp,
            createdBy = payment.createdBy.toPersonDTO(),
            amount = payment.amount,
            paidTo = payment.paidTo.toPersonDTO()
        )

        fun fromChange(expensesChanged: GroupExpensesChanged): ChangeDTO = ChangeDTO(
            id = expensesChanged.id,
            timeStamp = expensesChanged.timeStamp,
            createdBy = expensesChanged.createdBy.toPersonDTO(),
            groupExpenseEdited = fromExpense(expensesChanged.groupExpenseEdited),
            groupExpenseOriginal = fromExpense(expensesChanged.groupExpenseOriginal)
        )
    }

    fun toEvent() = when (this) {
        is ChangeDTO -> GroupExpensesChanged(
            id = id,
            createdBy = createdBy.toPerson(),
            timeStamp = timeStamp,
            groupExpenseOriginal = let {
                val expense = groupExpenseOriginal as ExpenseDTO
                GroupExpense(
                    id = expense.id,
                    timeStamp = expense.timeStamp,
                    createdBy = expense.createdBy.toPerson(),
                    description = expense.description,
                    sharedExpense = expense.sharedExpense,
                    payee = expense.payee.toPerson(),
                    individualExpenses = expense.individualExpenses.map { it.toIndividualExpense() }
                )
            },
            groupExpenseEdited = let {
                val expense = groupExpenseEdited as ExpenseDTO
                GroupExpense(
                    id = expense.id,
                    timeStamp = expense.timeStamp,
                    createdBy = expense.createdBy.toPerson(),
                    description = expense.description,
                    sharedExpense = expense.sharedExpense,
                    payee = expense.payee.toPerson(),
                    individualExpenses = groupExpenseEdited.individualExpenses.map { it.toIndividualExpense() }
                )
            }
        )
        is PaymentDTO -> Payment(
            id = id,
            createdBy = createdBy.toPerson(),
            timeStamp = timeStamp,
            paidTo = paidTo.toPerson(),
            amount = amount
        )
        is ExpenseDTO -> GroupExpense(
            id = id,
            timeStamp = timeStamp,
            createdBy = createdBy.toPerson(),
            payee = payee.toPerson(),
            individualExpenses = individualExpenses.map { it.toIndividualExpense() },
            sharedExpense = sharedExpense,
            description = description
        )
        else -> throw Exception("Unknown type")
    }
}
