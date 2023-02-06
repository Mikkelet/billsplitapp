package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.domain.models.Group
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetGroupFromLocalUseCase @Inject constructor(
    private val database: BillSplitDb
) {

    suspend fun execute(
        groupId: String
    ): Group {
        val groupDb = database.groupsDao().getGroup(groupId)
        return Group(groupDb)
    }
}