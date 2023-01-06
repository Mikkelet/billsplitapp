package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.domain.models.Friend
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetFriendsUseCase @Inject constructor(
    private val database: BillSplitDb,
    private val serverApiImpl: ServerApiImpl
) {

    suspend fun execute(sync: Boolean = false): List<Friend> {
        return if (sync) {
            val dtos = serverApiImpl.getFriends()
            val friendsDB = dtos.map { it.toDB() }
            database.friendsDao().insert(friendsDB)
            dtos.map { Friend.fromDTO(it) }
        } else {
            val friendsDb = database.friendsDao().getFriends()
            if (friendsDb.isEmpty())
                execute(true)
            else friendsDb.map { it.toFriend() }
        }
    }
}