package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetFriendsUseCase @Inject constructor(
    private val database: BillSplitDb,
    private val serverApiImpl: ServerApiImpl
) {

    suspend operator fun invoke() {
        val dtos = serverApiImpl.getFriends()
        val friendsDB = dtos.map { it.toDB() }
        database.friendsDao().clearTable()
        database.friendsDao().insert(friendsDB)
    }
}