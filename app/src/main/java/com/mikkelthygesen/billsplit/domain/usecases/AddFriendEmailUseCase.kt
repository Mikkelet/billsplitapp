package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.models.Friend
import javax.inject.Inject

class AddFriendEmailUseCase @Inject constructor(
    private val serverApiImpl: ServerApiImpl,
    private val database: BillSplitDb,
) {

    suspend fun execute(email:String):Friend{
        val dto = serverApiImpl.addFriendEmail("",email)
        database.friendsDao().insert(dto.toDB())
        return Friend.fromDTO(dto)
    }
}