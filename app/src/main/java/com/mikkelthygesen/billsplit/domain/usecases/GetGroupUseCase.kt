package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.SubscriptionServiceDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetGroupUseCase @Inject constructor(
    private val serverApiImpl: ServerApiImpl,
    private val database: BillSplitDb,
) {

    suspend fun execute(groupId: String, sync: Boolean = false): Group {
        if (sync) {
            val getGroupResponse = serverApiImpl.getGroup(groupId)
            val groupDto = getGroupResponse.group
            val eventDtos = getGroupResponse.events
            val serviceDTOs = getGroupResponse.services

            // Insert to db
            val groupExpensesDb = eventDtos.filterIsInstance<EventDTO.ExpenseDTO>()
            val paymentsDb = eventDtos.filterIsInstance<EventDTO.PaymentDTO>()
            val expenseChangesDb = eventDtos.filterIsInstance<EventDTO.ChangeDTO>()
            database.groupsDao().insert(groupDto.toDB())
            database.eventsDao().insertGroupExpenses(groupExpensesDb.map { it.toDb(groupDto.id) })
            database.eventsDao().insertPayments(paymentsDb.map { it.toDb(groupId) })
            database.eventsDao().insertExpenseChanges(expenseChangesDb.map { it.toDb(groupDto.id) })
            database.servicesDao().insert(serviceDTOs.map { SubscriptionServiceDb(groupId, it) })

            val events = eventDtos.map { it.toEvent() }
            val services = serviceDTOs.map { SubscriptionService(it) }
            return groupDto.toGroup().copy(events = events, services = services)
        } else {
            val group = database.groupsDao().getGroup(groupId).toGroup()
            val expenses: List<Event> =
                database.eventsDao().getGroupExpenses(groupId).map { it.toGroupExpense() }
            val payments: List<Event> =
                database.eventsDao().getPayments(groupId).map { it.toPayment() }
            val changes: List<Event> =
                database.eventsDao().getExpenseChanges(groupId).map { it.toExpenseChange() }
            val events = expenses.plus(payments).plus(changes).sortedBy { it.timeStamp }
            val services = database.servicesDao().getServices(groupId).map { SubscriptionService(it) }
            return group.copy(events = events, services = services)
        }
    }
}