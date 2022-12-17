package com.mikkelthygesen.billsplit.data.network.requests

import com.mikkelthygesen.billsplit.data.network.dto.EventDTO
import com.mikkelthygesen.billsplit.data.network.dto.GroupDTO
import com.mikkelthygesen.billsplit.models.Group

object GetGroup {
    @kotlinx.serialization.Serializable
    data class Request(
        val groupId: String
    )

    @kotlinx.serialization.Serializable
    data class Response(
        val group: GroupDTO,
        val events: List<EventDTO>,
    ) {

        fun toGroup(): Group {
            return Group(
                id = group.id,
                name = group.name,
                people = group.people.map { it.toPerson() },
                createdBy = group.createdBy.toPerson(),
                timeStamp = group.timeStamp,
                events = events.map { it.toEvent() },
                debts = group.debts.map { it.toDebt() }
            )
        }
    }
}