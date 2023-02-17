package com.mikkelthygesen.billsplit.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.data.local.database.model.GroupExpenseDb
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import com.mikkelthygesen.billsplit.reduceOrZero
import com.mikkelthygesen.billsplit.tryCatchDefault

data class GroupExpense(
    override val id: String = "",
    override val createdBy: Person,
    private var description: String = "",
    private var payee: Person,
    private var sharedExpense: Float = 0F,
    val individualExpenses: List<IndividualExpense>,
    override val timeStamp: Long = System.currentTimeMillis()
) : Event {

    constructor(expenseDb: GroupExpenseDb) : this(
        id = expenseDb.id,
        createdBy = Person(expenseDb.createdBy),
        description = expenseDb.description,
        payee = Person(expenseDb.payee),
        sharedExpense = expenseDb.sharedExpense,
        individualExpenses = expenseDb.individualExpenses.map { IndividualExpense(it) },
        timeStamp = expenseDb.timeStamp,
    )

    constructor(expenseDTO: EventDTO.ExpenseDTO) : this(
        id = expenseDTO.id,
        createdBy = Person(expenseDTO.createdBy),
        description = expenseDTO.description,
        payee = Person(expenseDTO.payee),
        sharedExpense = expenseDTO.sharedExpense,
        individualExpenses = expenseDTO.individualExpenses.map { IndividualExpense(it) },
        timeStamp = expenseDTO.timeStamp,
    )

    var descriptionState by mutableStateOf(description)
    var payeeState by mutableStateOf(payee)
    var sharedExpenseState by mutableStateOf(sharedExpense)

    val total: Float
        get() = individualExpenses.map { it.expenseState }.reduceOrZero() + sharedExpenseState
    private val sharedExpensePerParticipant
        get() = tryCatchDefault(0F) { sharedExpenseState / participants }
    private val participants: Int
        get() = individualExpenses.count { it.isParticipantState }

    fun getParticipants() = individualExpenses.filter { it.isParticipantState }.map { it.person }

    fun getIndividualExpensesWithShared() = individualExpenses.map { ie ->
        ie.copy(
            expense =
            if (ie.isParticipantState)
                ie.expenseState + sharedExpensePerParticipant
            else ie.expenseState
        )
    }

    fun isChanged(): Boolean {
        return sharedExpense != sharedExpenseState
                || individualExpenses.any { it.isChanged() }
                || descriptionState != description
                || payeeState != payee
    }

    fun revertChanges() {
        descriptionState = description
        payeeState = payee
        sharedExpenseState = sharedExpense
        individualExpenses.forEach { it.revertChanges() }
    }

    fun copy(): GroupExpense = GroupExpense(
        id,
        createdBy.copy(),
        descriptionState,
        payeeState.copy(),
        sharedExpense,
        individualExpenses.map { it.copy() },
        timeStamp
    )

    fun original() = GroupExpense(
        id,
        createdBy,
        description,
        payee,
        sharedExpense,
        individualExpenses.map { it.original() },
        timeStamp
    )

}