package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.domain.models.GroupExpensesChanged

@Entity(tableName = "expense_changes")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
data class ExpenseChangeDb(
    @PrimaryKey
    val id: String,
    val groupId: String,
    @Embedded(prefix = "createdby_")
    val createdBy: PersonDb,
    @Embedded(prefix = "original_")
    val groupExpenseOriginal: GroupExpenseDb,
    @Embedded(prefix = "edited_")
    val groupExpenseEdited: GroupExpenseDb,
    val timeStamp: Long,
) {

    fun toExpenseChange() = GroupExpensesChanged(
        id = id,
        createdBy = createdBy.toPerson(),
        groupExpenseOriginal = groupExpenseOriginal.toGroupExpense(),
        groupExpenseEdited = groupExpenseEdited.toGroupExpense(),
        timeStamp = timeStamp
    )
}