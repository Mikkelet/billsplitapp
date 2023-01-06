package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.DebtDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.domain.models.Group

@Entity(tableName = "groups")
data class GroupDb(
    @PrimaryKey
    val id: String,
    val name: String,
    @Embedded(prefix = "createdby_")
    val createdBy: PersonDb,
    val people: List<PersonDb>,
    val timestamp: Long,
    val debts: List<DebtDb>
) {

    fun toGroup() = Group(
        id = id,
        name = name,
        timeStamp = timestamp,
        people = people.map { it.toPerson() },
        debts = debts.map { it.toDebt() }
    )
}