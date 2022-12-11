package com.mikkelthygesen.billsplit.data.network

import com.mikkelthygesen.billsplit.data.network.dto.*
import com.mikkelthygesen.billsplit.data.network.requests.*
import com.mikkelthygesen.billsplit.models.*
import com.mikkelthygesen.billsplit.models.interfaces.Event

@Suppress("OPT_IN_USAGE")
class ServerApiImpl {

    suspend fun addGroup(group: Group): Group {
        val groupDTO = GroupDTO.fromGroup(group)
        val addGroupDTO = AddGroup.Request(groupDTO)
        return ServerApi.addGroup(addGroupDTO).group.toGroup()
    }

    suspend fun addEvent(groupId: String, event: Event): Event {
        val eventDto: EventDTO = when (event) {
            is GroupExpense -> EventDTO.fromExpense(event)
            is Payment -> EventDTO.fromPayment(event)
            is GroupExpensesChanged -> EventDTO.fromChange(event)
            else -> throw Exception("Invalid event")
        }
        return ServerApi.addEvent(AddEvent.Request(groupId, eventDto)).event.toEvent()
    }

    suspend fun getGroup(groupId: String): Group {
        val dto = ServerApi.getGroup(GetGroup.Request(groupId))
        return dto.toGroup()
    }

    suspend fun getGroups(userId: String): List<Group> {
        val dtos = ServerApi.getGroups(GetGroups.Request(userId))
        return dtos.groups.map { it.toGroup() }
    }

    suspend fun addFriendUserId(loggedInUserId: String, user: Person): Friend {
        val request = AddFriend.Request.UserId(
            loggedInUserId,
            System.currentTimeMillis(),
            PersonDTO.fromPerson(user),
        )
        val friendDTO = ServerApi.addFriend(request).friend
        return Friend.fromDTO(friendDTO)
    }

    suspend fun addFriendEmail(loggedInUserId: String, email: String): Friend {
        val request = AddFriend.Request.Email(
            loggedInUserId,
            System.currentTimeMillis(),
            email,
        )
        val friendDTO = ServerApi.addFriend(request).friend
        return Friend.fromDTO(friendDTO)
    }

    suspend fun getFriends(userId: String): List<Friend> {
        val request = GetFriends.Request(userId)
        return ServerApi.getFriends(request).friends.map { Friend.fromDTO(it) }
    }

    suspend fun updateUser(user: Person) {
        val dto = PersonDTO.fromPerson(user)
        return ServerApi.updateUser(UpdateUser.Request(dto))
    }
}