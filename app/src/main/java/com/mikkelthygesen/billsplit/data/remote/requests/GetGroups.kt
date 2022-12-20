package com.mikkelthygesen.billsplit.data.remote.requests

import com.mikkelthygesen.billsplit.data.remote.dto.GroupDTO

object GetGroups {

    @kotlinx.serialization.Serializable
    data class Response(
        val groups: List<GroupDTO>
    )
}