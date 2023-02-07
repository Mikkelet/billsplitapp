package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.domain.models.Group
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class ObserveDatabaseGroupsUseCase @Inject constructor(
    private val database: BillSplitDb,
) {
    fun execute(): Flow<List<Group>> {
        return database.groupsDao().getGroupsFlow()
            .map {
                it.map { groupDb -> Group(groupDb) }
            }
    }
}