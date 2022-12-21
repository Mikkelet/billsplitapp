package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.interfaces.Event
import javax.inject.Inject

class AddEventUseCase @Inject constructor(
    private val database: BillSplitDb,
    private val serverApiImpl: ServerApiImpl
) {

    suspend fun execute(group: Group, event: Event): Event {
        val dto = serverApiImpl.addEvent(group, event)
        database.groupsDao().insert(group.toDb())
        return dto.toEvent()
    }
}