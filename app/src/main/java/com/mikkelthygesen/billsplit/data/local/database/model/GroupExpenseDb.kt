package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.IndividualExpenseDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.models.GroupExpense

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
) {

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