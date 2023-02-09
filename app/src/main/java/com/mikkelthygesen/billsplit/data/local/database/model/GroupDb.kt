package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.DebtDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.data.remote.dto.GroupDTO
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
    constructor(group: Group, debts: List<Pair<String, Float>>) : this(
        id = group.id,
        name = group.nameState,
        createdBy = group.createdBy.toDb(),
        timestamp = group.timeStamp,
        people = group.peopleState.map { PersonDb(it) },
        debts = debts.map { DebtDb(it.first, it.second) }
    )

    constructor(group: GroupDTO) : this(
        id = group.id,
        name = group.name,
        createdBy = PersonDb(group.createdBy),
        timestamp = group.timeStamp,
        people = group.people.map { PersonDb(it) },
        debts = group.debts.map { DebtDb(it.userId, it.owes) }
    )
}