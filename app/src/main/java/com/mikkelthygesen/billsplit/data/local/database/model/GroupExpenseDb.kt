package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.IndividualExpenseDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.domain.models.GroupExpense

@Entity(tableName = "group_expenses")
class GroupExpenseDb(
    @PrimaryKey
    val id: String,
    val groupId: String,
    @Embedded(prefix = "createdby_")
    val createdBy: PersonDb,
    val description: String,
    @Embedded(prefix = "payee_")
    val payee: PersonDb,
    val sharedExpense: Float,
    val individualExpenses: List<IndividualExpenseDb>,
    val timeStamp: Long
) : EventDb {

    constructor(groupId: String, expenseDTO: EventDTO.ExpenseDTO) : this(
        groupId = groupId,
        id = expenseDTO.id,
        createdBy = PersonDb(expenseDTO.createdBy),
        description = expenseDTO.description,
        payee = expenseDTO.payee.toDB(),
        sharedExpense = expenseDTO.sharedExpense,
        individualExpenses = expenseDTO.individualExpenses.map { IndividualExpenseDb(it) },
        timeStamp = expenseDTO.timeStamp
    )

    constructor(groupId: String, expense: GroupExpense) : this(
        groupId = groupId,
        id = expense.id,
        createdBy = PersonDb(expense.createdBy),
        description = expense.descriptionState,
        payee = PersonDb(expense.payeeState),
        sharedExpense = expense.sharedExpenseState,
        individualExpenses = expense.individualExpenses.map { IndividualExpenseDb(it) },
        timeStamp = expense.timeStamp
    )

    fun toGroupExpense() = GroupExpense(
        id = id,
        createdBy = createdBy.toPerson(),
        description = description,
        payee = payee.toPerson(),
        sharedExpense = sharedExpense,
        individualExpenses = individualExpenses.map { it.toIndividualExpense() },
        timeStamp = timeStamp
    )
}