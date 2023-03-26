package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupExpenseDb
import com.mikkelthygesen.billsplit.data.local.database.model.PaymentDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DeleteEventUseCase @Inject constructor(
    private val billSplitDb: BillSplitDb,
    private val serverApiImpl: ServerApiImpl,
    private val getDebtForGroupUseCase: GetDebtForGroupUseCase,
) {

    suspend operator fun invoke(groupId: String, event: Event) {
        val debts = getDebtForGroupUseCase.execute(groupId, event, isDelete = true)
        serverApiImpl.deleteEvent(groupId, event, debts)
        when(event){
            is GroupExpense -> {
                val eventDb = GroupExpenseDb(groupId, event)
                billSplitDb.groupExpensesDao().delete(eventDb)
            }
            is Payment -> {
                val eventDb = PaymentDb(groupId, event)
                billSplitDb.paymentsDao().delete(eventDb)
            }
            else -> return
        }
    }
}