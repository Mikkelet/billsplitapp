package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.DebtDb
import com.mikkelthygesen.billsplit.models.Group

@Entity(tableName = "groups")
data class GroupDb(
    @PrimaryKey
    val id: String,
    val name: String,
    val timestamp: Long,
    val debts: List<DebtDb>
) {

    fun toGroup() = Group(
        id = id,
        name = name,
        timeStamp = timestamp,
        debts = debts.map { it.toDebt() }
    )
}