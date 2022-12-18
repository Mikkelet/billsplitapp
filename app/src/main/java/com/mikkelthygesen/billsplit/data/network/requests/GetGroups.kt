package com.mikkelthygesen.billsplit.data.network.requests

import com.mikkelthygesen.billsplit.data.network.dto.GroupDTO

object GetGroups {

    @kotlinx.serialization.Serializable
    data class Response(
        val groups: List<GroupDTO>
    )
}