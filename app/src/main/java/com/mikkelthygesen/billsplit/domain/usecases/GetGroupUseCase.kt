package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Group
import javax.inject.Inject

class GetGroupUseCase @Inject constructor(
    private val serverApiImpl: ServerApiImpl,
    private val database: BillSplitDb,
) {

    suspend fun execute(groupId: String): Group {
        val group = serverApiImpl.getGroup(groupId).toGroup()
        database.groupsDao().insert(group.toDb())
        return group
    }
}