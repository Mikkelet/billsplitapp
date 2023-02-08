package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.ExpenseChangeDb
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
    private val serverApiImpl: ServerApiImpl
) {

    suspend fun execute(group: Group, event: Event): Event {
        val dto = serverApiImpl.addEvent(group, event)
        database.groupsDao().insert(group.toDb())
        when (dto) {
            is EventDTO.ExpenseDTO -> database.groupExpensesDao()
                .insert(GroupExpenseDb(group.id, dto))
            is EventDTO.ChangeDTO -> database.expenseChangesDao()
                .insert(ExpenseChangeDb(group.id, dto))
            is EventDTO.PaymentDTO -> database.paymentsDao().insert(PaymentDb(group.id, dto))
        }
        return dto.toEvent()
    }
}