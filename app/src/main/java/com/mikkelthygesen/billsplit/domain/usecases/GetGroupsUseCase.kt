package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetGroupsUseCase @Inject constructor(
    private val database: BillSplitDb,
    private val serverApiImpl: ServerApiImpl
) {
    suspend operator fun invoke() {
        val dtos = serverApiImpl.getGroups()
        database.groupsDao().clearTable()
        database.groupsDao().insert(dtos.map { GroupDb(it) })
    }
}