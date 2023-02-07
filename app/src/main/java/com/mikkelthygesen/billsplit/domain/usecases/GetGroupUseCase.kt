package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.ExpenseChangeDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupExpenseDb
import com.mikkelthygesen.billsplit.data.local.database.model.PaymentDb
import com.mikkelthygesen.billsplit.data.local.database.model.SubscriptionServiceDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.domain.models.Group
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
            val groupExpensesDTO = eventDtos.filterIsInstance<EventDTO.ExpenseDTO>()
            val paymentsDTO = eventDtos.filterIsInstance<EventDTO.PaymentDTO>()
            val expenseChangesDTO = eventDtos.filterIsInstance<EventDTO.ChangeDTO>()
            // Clear
            database.groupsDao().clearTable()
            database.eventsDao().clearChangesTable()
            database.eventsDao().clearExpensesTable()
            database.eventsDao().clearPaymentTable()
            database.servicesDao().clearTable()
            // Add new
            database.groupsDao().insert(groupDto.toDB())
            database.eventsDao()
                .insertGroupExpenses(groupExpensesDTO.map { GroupExpenseDb(groupId, it) })
            database.eventsDao().insertPayments(paymentsDTO.map { PaymentDb(groupId, it) })
            database.eventsDao()
                .insertExpenseChanges(expenseChangesDTO.map { ExpenseChangeDb(groupId, it) })
            database.servicesDao().insert(serviceDTOs.map { SubscriptionServiceDb(groupId, it) })

            return Group(groupDto)
        } else {
            val groupDb = database.groupsDao().getGroup(groupId)
            return Group(groupDb)
        }
    }
}