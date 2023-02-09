package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.domain.models.Friend
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class ObserveLocalFriendsUseCase @Inject constructor(
    private val billSplitDb: BillSplitDb
) {

    operator fun invoke(): Flow<List<Friend>> {
        return billSplitDb.friendsDao().getFriendsFlow().map { friendDbs ->
            friendDbs.map { it.toFriend() }
        }
    }
}