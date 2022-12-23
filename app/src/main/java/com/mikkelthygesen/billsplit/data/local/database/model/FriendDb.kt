package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.data.remote.dto.FriendStatusDTO
import com.mikkelthygesen.billsplit.models.Friend

@Entity(tableName = "friends")
data class FriendDb(
    @PrimaryKey
    val id: String,
    val status: String,
    val createdBy: String,
    @Embedded
    val friend: PersonDb,
) {
    fun toFriend(): Friend = when (status) {
        FriendStatusDTO.STATUS_ACCEPTED -> Friend.FriendAccepted(friend.toPerson())
        else -> if (createdBy == friend.uid) Friend.FriendRequestReceived(
            friend.toPerson()
        )
        else Friend.FriendRequestSent(friend.toPerson())
    }
}