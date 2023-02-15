package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.DebtDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.LatestEventDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.data.remote.dto.GroupDTO
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.domain.models.Payment

@Entity(tableName = "groups")
data class GroupDb(
    @PrimaryKey
    val id: String,
    val name: String,
    @Embedded(prefix = "createdby_")
    val createdBy: PersonDb,
    val people: List<PersonDb>,
    val timestamp: Long,
    val debts: List<DebtDb>,
    @Embedded("latestEvent_")
    val latestEvent: LatestEventDb
) {
    constructor(group: Group, debts: List<Pair<String, Float>>) : this(
        id = group.id,
        name = group.nameState,
        createdBy = group.createdBy.toDb(),
        timestamp = group.timeStamp,
        people = group.peopleState.map { PersonDb(it) },
        debts = debts.map { DebtDb(it.first, it.second) },
        latestEvent = LatestEventDb(
            id = group.latestEvent?.id,
            type = when (group.latestEvent) {
                is GroupExpense -> EVENT_TYPE_EXPENSE
                is Payment -> EVENT_TYPE_PAYMENT
                is GroupExpensesChanged -> EVENT_TYPE_CHANGE
                else -> null
            }
        )
    )

    constructor(group: GroupDTO) : this(
        id = group.id,
        name = group.name,
        createdBy = PersonDb(group.createdBy),
        timestamp = group.timeStamp,
        people = group.people.map { PersonDb(it) },
        debts = group.debts.map { DebtDb(it.userId, it.owes) },
        latestEvent = LatestEventDb(
            id = group.latestEvent?.toEvent()?.id,
            type = when (group.latestEvent) {
                is EventDTO.PaymentDTO -> EVENT_TYPE_PAYMENT
                is EventDTO.ExpenseDTO -> EVENT_TYPE_EXPENSE
                is EventDTO.ChangeDTO -> EVENT_TYPE_CHANGE
                else -> null
            }
        )
    )

    companion object {
        const val EVENT_TYPE_PAYMENT = "payment"
        const val EVENT_TYPE_EXPENSE = "expense"
        const val EVENT_TYPE_CHANGE = "change"
    }
}