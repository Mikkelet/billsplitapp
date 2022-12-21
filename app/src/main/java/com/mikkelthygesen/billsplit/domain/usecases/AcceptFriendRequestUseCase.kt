package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import com.mikkelthygesen.billsplit.data.remote.exceptions.NetworkExceptions
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Person
import javax.inject.Inject

class AcceptFriendRequestUseCase @Inject constructor(
    private val authProvider: AuthProvider,
    private val serverApiImpl: ServerApiImpl,
    private val database: BillSplitDb,
) {

    suspend fun execute(friend: Person): Friend {
        val user = authProvider.loggedInUser ?: throw NetworkExceptions.UserLoggedOutException
        val dto = serverApiImpl.acceptFriendRequest(user.uid, friend)
        database.friendsDao().insert(dto.toDB())
        return Friend.fromDTO(dto)
    }
}