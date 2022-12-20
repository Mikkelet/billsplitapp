package com.mikkelthygesen.billsplit.data.remote.requests

import com.mikkelthygesen.billsplit.data.remote.dto.FriendDTO

object GetFriends {

    @kotlinx.serialization.Serializable
    data class Response(val friends: List<FriendDTO>)
}