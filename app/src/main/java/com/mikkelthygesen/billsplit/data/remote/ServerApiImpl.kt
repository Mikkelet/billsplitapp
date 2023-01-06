package com.mikkelthygesen.billsplit.data.remote

import com.mikkelthygesen.billsplit.data.remote.dto.*
import com.mikkelthygesen.billsplit.data.remote.requests.*
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.models.*
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
        val groupDTO = GroupDTO.fromGroup(group)
        val addGroupDTO = AddGroup.Request(groupDTO)
        return serverApi.addGroup(addGroupDTO).group
    }

    suspend fun addEvent(
        group: Group,
        event: Event,
    ): EventDTO {
        val eventDto: EventDTO = when (event) {
            is GroupExpense -> EventDTO.fromExpense(event)
            is Payment -> EventDTO.fromPayment(event)
            is GroupExpensesChanged -> EventDTO.fromChange(event)
            else -> throw Exception("Invalid event")
        }
        val debtsDto = group.debtsState.map {
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

    suspend fun getGroup(groupId: String): Pair<GroupDTO, List<EventDTO>> {
        val dto = serverApi.getGroup(GetGroup.Request(groupId))
        return dto.group to dto.events
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
}