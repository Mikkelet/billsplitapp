package com.mikkelthygesen.billsplit.data.remote.requests

import com.mikkelthygesen.billsplit.data.remote.dto.DebtDTO
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO

object AddEvent {

    @kotlinx.serialization.Serializable
    data class Request(
        val groupId: String,
        val event: EventDTO,
        val debts: List<DebtDTO>
    )

    @kotlinx.serialization.Serializable
    data class Response(
        val event: EventDTO
    )
}