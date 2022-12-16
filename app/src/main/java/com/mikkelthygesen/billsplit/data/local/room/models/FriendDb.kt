package com.mikkelthygesen.billsplit.data.local.room.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.mikkelthygesen.billsplit.data.network.dto.FriendStatusDTO
import com.mikkelthygesen.billsplit.models.Friend

@Entity(tableName = "friends")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
data class FriendDb(
    @PrimaryKey
    val id: String,
    val timeStamp: Long,
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