package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Group
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetGroupUseCase @Inject constructor(
    private val serverApiImpl: ServerApiImpl,
    private val database: BillSplitDb,
) {

    suspend fun execute(groupId: String): Group {
        val groupAndEvents = serverApiImpl.getGroup(groupId)
        database.groupsDao().insert(groupAndEvents.first.toDB())
        val events = groupAndEvents.second.map { it.toEvent() }
        return groupAndEvents.first.toGroup().copy(events = events)
    }
}