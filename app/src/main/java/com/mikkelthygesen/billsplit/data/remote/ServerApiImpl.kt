package com.mikkelthygesen.billsplit.data.remote

import com.mikkelthygesen.billsplit.data.remote.dto.*
import com.mikkelthygesen.billsplit.data.remote.requests.*
import com.mikkelthygesen.billsplit.domain.models.*
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import io.ktor.client.*
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("OPT_IN_USAGE")
@Singleton
class ServerApiImpl @Inject constructor(
    private val serverApi: ServerApi
) {

    suspend fun addGroup(group: Group): GroupDTO {
        val groupDTO = GroupDTO(group)
        val addGroupDTO = AddGroup.Request(groupDTO)
        return serverApi.addGroup(addGroupDTO).group
    }

    suspend fun addEvent(
        group: Group,
        event: Event,
        debts: List<Pair<String, Float>>
    ): EventDTO {
        val eventDto: EventDTO = EventDTO.fromEvent(event)
        val debtsDto = debts.map {
            DebtDTO(
                userId = it.first,
                owes = it.second
            )
        }
        val result = serverApi.addEvent(
            AddEvent.Request(
                group.id,
                eventDto,
                debtsDto
            )
        )
        return result.event
    }

    suspend fun deleteEvent(
        groupId: String, event: Event, debts: List<Pair<String, Float>>
    ) {
        val eventDto: EventDTO = EventDTO.fromEvent(event)
        val debtsDto = debts.map {
            DebtDTO(
                userId = it.first,
                owes = it.second
            )
        }
        val request = DeleteEvent.Request(groupId, eventDto, debtsDto)
        serverApi.deleteEvent(request)
    }

    suspend fun getGroup(groupId: String): GetGroup.Response {
        return serverApi.getGroup(GetGroup.Request(groupId))
    }

    suspend fun getGroups(): List<GroupDTO> = serverApi.getGroups().groups

    suspend fun addFriendEmail(email: String): FriendDTO {
        val request = AddFriend.Request.Email(email)
        return serverApi.addFriend(request).friend
    }

    suspend fun acceptFriendRequest(friendId: String): FriendDTO {
        val request = AddFriend.Request.UserId(friendId)
        return serverApi.addFriend(request).friend
    }

    suspend fun getFriends(): List<FriendDTO> {
        return serverApi.getFriends().friends
    }

    suspend fun addSubscriptionService(groupId: String, service: SubscriptionService): ServiceDTO {
        val request = AddSubscriptionService.Request(groupId, service = ServiceDTO(service))
        return serverApi.addSubscriptionService(request).service
    }

    suspend fun updateSubscriptionService(groupId: String, service: SubscriptionService) {
        val request = UpdateSubscriptionService.Request(groupId, ServiceDTO(service))
        serverApi.updateSubscriptionService(request)
    }
}