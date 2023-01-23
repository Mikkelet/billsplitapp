package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupExpenseDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddEventUseCase @Inject constructor(
    private val database: BillSplitDb,
    private val serverApiImpl: ServerApiImpl
) {

    suspend fun execute(group: Group, event: Event): Event {
        val dto = serverApiImpl.addEvent(group, event)
        database.groupsDao().insert(group.toDb())
        when (dto) {
            is EventDTO.ExpenseDTO -> database.eventsDao().insertGroupExpense(dto.toDb(group.id))
            is EventDTO.ChangeDTO -> database.eventsDao().insertExpenseChange(dto.toDb(group.id))
            is EventDTO.PaymentDTO -> database.eventsDao().insertPayment(dto.toDb(group.id))
        }
        return dto.toEvent()
    }
}