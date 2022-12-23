package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.interfaces.Event
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetGroupUseCase @Inject constructor(
    private val serverApiImpl: ServerApiImpl,
    private val database: BillSplitDb,
) {

    suspend fun execute(groupId: String, sync: Boolean = false): Group {
        if (sync) {
            val groupAndEvents = serverApiImpl.getGroup(groupId)
            val groupDto = groupAndEvents.first

            // Insert to db
            val groupExpensesDb = groupAndEvents.second.filterIsInstance<EventDTO.ExpenseDTO>()
            val paymentsDb = groupAndEvents.second.filterIsInstance<EventDTO.PaymentDTO>()
            val expenseChangesDb = groupAndEvents.second.filterIsInstance<EventDTO.ChangeDTO>()
            database.groupsDao().insert(groupDto.toDB())
            database.eventsDao().insertGroupExpenses(groupExpensesDb.map { it.toDb(groupDto.id) })
            database.eventsDao().insertPayments(paymentsDb.map { it.toDb(groupId) })
            database.eventsDao().insertExpenseChanges(expenseChangesDb.map { it.toDb(groupDto.id) })

            val events = groupAndEvents.second.map { it.toEvent() }
            return groupAndEvents.first.toGroup().copy(events = events)
        } else {
            val group = database.groupsDao().getGroup(groupId).toGroup()
            val expenses: List<Event> =
                database.eventsDao().getGroupExpenses(groupId).map { it.toGroupExpense() }
            val payments: List<Event> =
                database.eventsDao().getPayments(groupId).map { it.toPayment() }
            val changes: List<Event> =
                database.eventsDao().getExpenseChanges(groupId).map { it.toExpenseChange() }
            val events = expenses.plus(payments).plus(changes).sortedBy { it.timeStamp }
            if (events.isEmpty())
                return execute(groupId, true)
            return group.copy(events = events)
        }
    }
}