package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.*
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.domain.latestEvent
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
            database.expenseChangesDao().clearTableForGroup(groupId)
            database.paymentsDao().clearTableForGroup(groupId)
            database.groupExpensesDao().clearTableForGroup(groupId)
            database.servicesDao().clearTable(groupId)
            // Add new
            database.groupsDao().insert(GroupDb(groupDto))
            database.groupExpensesDao().insert(groupExpensesDTO.map { GroupExpenseDb(groupId, it) })
            database.paymentsDao().insert(paymentsDTO.map { PaymentDb(groupId, it) })
            database.expenseChangesDao()
                .insert(expenseChangesDTO.map { ExpenseChangeDb(groupId, it) })
            database.servicesDao().insert(serviceDTOs.map { SubscriptionServiceDb(groupId, it) })

            return Group(groupDto)
        } else {
            val groupDb = database.groupsDao().getGroup(groupId)
            return Group(groupDb, groupDb.latestEvent(database))
        }
    }
}