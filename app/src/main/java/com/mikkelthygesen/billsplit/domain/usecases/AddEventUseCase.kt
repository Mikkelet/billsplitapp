package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.ExpenseChangeDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupExpenseDb
import com.mikkelthygesen.billsplit.data.local.database.model.PaymentDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddEventUseCase @Inject constructor(
    private val database: BillSplitDb,
    private val serverApiImpl: ServerApiImpl,
    private val getDebtForGroupUseCase: GetDebtForGroupUseCase
) {

    suspend fun execute(group: Group, event: Event): Event {
        val debts = getDebtForGroupUseCase.execute(group.id, event)
        val eventDTO = serverApiImpl.addEvent(group, event, debts)
        val eventWithId = eventDTO.toEvent()
        val groupWithLatestEvent = group.copy(latestEvent = eventWithId)
        database.groupsDao().insert(GroupDb(groupWithLatestEvent, debts))
        when (eventDTO) {
            is EventDTO.ExpenseDTO -> database.groupExpensesDao()
                .insert(GroupExpenseDb(group.id, eventDTO))
            is EventDTO.ChangeDTO -> database.expenseChangesDao()
                .insert(ExpenseChangeDb(group.id, eventDTO))
            is EventDTO.PaymentDTO -> database.paymentsDao().insert(PaymentDb(group.id, eventDTO))
        }
        return eventWithId
    }
}