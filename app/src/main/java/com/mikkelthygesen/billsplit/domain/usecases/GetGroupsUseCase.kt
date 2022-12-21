package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Group
import javax.inject.Inject

class GetGroupsUseCase @Inject constructor(
    private val database: BillSplitDb,
    private val serverApiImpl: ServerApiImpl
) {
    suspend fun execute(sync: Boolean): List<Group> {
        return if (sync) {
            val dtos = serverApiImpl.getGroups()
            database.groupsDao().insert(dtos.map { it.toDB() })
            dtos.map { it.toGroup() }
        } else {
            val groupsDb = database.groupsDao().getGroups()
            if (groupsDb.isEmpty())
                execute(true)
            else groupsDb.map { it.toGroup() }
        }
    }
}