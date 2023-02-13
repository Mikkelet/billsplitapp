package com.mikkelthygesen.billsplit.data.remote.requests

import com.mikkelthygesen.billsplit.data.remote.dto.ServiceDTO

object UpdateSubscriptionService {

    @kotlinx.serialization.Serializable
    data class Request(
        val groupId: String,
        val service: ServiceDTO
    )
}