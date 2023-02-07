package com.mikkelthygesen.billsplit.data.remote.requests

import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.data.remote.dto.GroupDTO
import com.mikkelthygesen.billsplit.data.remote.dto.ServiceDTO

object GetGroup {
    @kotlinx.serialization.Serializable
    data class Request(
        val groupId: String
    )

    @kotlinx.serialization.Serializable
    data class Response(
        val group: GroupDTO,
        val events: List<EventDTO>,
        val services: List<ServiceDTO>
    )
}