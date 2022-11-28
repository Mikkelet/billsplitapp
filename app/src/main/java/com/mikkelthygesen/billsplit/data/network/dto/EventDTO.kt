package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull.serializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@kotlinx.serialization.Serializable(with = EventDTO.EventDTOSerializer::class)
sealed class EventDTO {

    abstract val eventType: String

    @kotlinx.serialization.Serializable
    data class ExpenseDTO(
        override val eventType: String = TYPE_EXPENSE,
        val id: String,
        val createdBy: String,
        val timeStamp: Long,
        val description: String,
        val payee: String,
        val sharedExpense: Float,
        val individualExpenses: List<IndividualExpenseDTO>,
    ) : EventDTO()

    @kotlinx.serialization.Serializable
    data class PaymentDTO(
        override val eventType: String = TYPE_PAYMENT,
        val id: String,
        val createdBy: String,
        val timeStamp: Long,
        val paidTo: String,
        val amount: Float,
    ) : EventDTO()

    @kotlinx.serialization.Serializable
    data class ChangeDTO(
        override val eventType: String = TYPE_CHANGE,
        val createdBy: String,
        val timeStamp: Long,
        val groupExpenseOriginal: ExpenseDTO,
        val groupExpenseEdited: ExpenseDTO
    ) : EventDTO()


    companion object {
        const val TYPE_PAYMENT = "payment"
        const val TYPE_CHANGE = "change"
        const val TYPE_EXPENSE = "expense"

        fun fromExpense(expense: GroupExpense) = ExpenseDTO(
            id = expense.id,
            description = expense.descriptionState,
            createdBy = expense.createdBy.uid,
            sharedExpense = expense.sharedExpenseState,
            individualExpenses = expense.individualExpenses.map {
                IndividualExpenseDTO.fromIndividualExpense(
                    it
                )
            },
            payee = expense.payeeState.uid,
            timeStamp = expense.timeStamp
        )

        fun fromPayment(payment: Payment) = PaymentDTO(
            id = "",
            timeStamp = payment.timeStamp,
            createdBy = payment.createdBy.uid,
            amount = payment.amount,
            paidTo = payment.paidTo.uid
        )

        fun fromChange(expensesChanged: GroupExpensesChanged) = ChangeDTO(
            timeStamp = expensesChanged.timeStamp,
            createdBy = expensesChanged.createdBy.uid,
            groupExpenseEdited = fromExpense(expensesChanged.groupExpenseEdited),
            groupExpenseOriginal = fromExpense(expensesChanged.groupExpenseOriginal)
        )
    }

    object EventDTOSerializer : JsonContentPolymorphicSerializer<EventDTO>(EventDTO::class) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out EventDTO> {
            return when (element.jsonObject["eventType"]?.jsonPrimitive?.content) {
                TYPE_CHANGE -> ChangeDTO.serializer()
                TYPE_PAYMENT -> PaymentDTO.serializer()
                TYPE_EXPENSE -> ExpenseDTO.serializer()
                else -> throw Exception("Unknown Module: key 'type' not found or does not matches any module type")
            }
        }
    }
}

