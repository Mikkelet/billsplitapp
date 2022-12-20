package com.mikkelthygesen.billsplit.data.network

import com.mikkelthygesen.billsplit.data.local.room.models.GroupDb
import com.mikkelthygesen.billsplit.data.network.dto.*
import com.mikkelthygesen.billsplit.data.network.requests.*
import com.mikkelthygesen.billsplit.db
import com.mikkelthygesen.billsplit.models.*
import com.mikkelthygesen.billsplit.models.interfaces.Event

@Suppress("OPT_IN_USAGE")
class ServerApiImpl {

    suspend fun addGroup(group: Group): Group {
        val groupDTO = GroupDTO.fromGroup(group)
        val addGroupDTO = AddGroup.Request(groupDTO)
        val response = ServerApi.addGroup(addGroupDTO).group
        db.groupsDao().insert(response.toDB())
        return response.toGroup()
    }

    suspend fun addEvent(
        group: Group,
        event: Event,
    ): Event {
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
        val result = ServerApi.addEvent(
            AddEvent.Request(
                group.id,
                eventDto,
                debtsDto
            )
        )
        db.groupsDao().insert(group.toDb())
        return result.event.toEvent()
    }

    suspend fun getGroup(groupId: String): Group {
        val dto = ServerApi.getGroup(GetGroup.Request(groupId))
        db.groupsDao().insert(dto.group.toDB())
        return dto.toGroup()
    }

    suspend fun getGroups(sync: Boolean = false): List<Group> {
        return if (sync) {
            val dtos = ServerApi.getGroups()
            db.groupsDao().insert(dtos.groups.map { it.toDB() })
            dtos.groups.map { it.toGroup() }
        } else {
            val groupsDb = db.groupsDao().getGroups()
            if (groupsDb.isEmpty())
                getGroups(true)
            else groupsDb.map { it.toGroup() }
        }
    }

    suspend fun addFriendUserId(loggedInUserId: String, user: Person): Friend {
        val request = AddFriend.Request.UserId(
            loggedInUserId,
            System.currentTimeMillis(),
            PersonDTO.fromPerson(user),
        )
        val friendDTO = ServerApi.addFriend(request).friend
        db.friendsDao().insert(friendDTO.toDB())
        return Friend.fromDTO(friendDTO)
    }

    suspend fun addFriendEmail(loggedInUserId: String, email: String): Friend {
        val request = AddFriend.Request.Email(
            loggedInUserId,
            System.currentTimeMillis(),
            email,
        )
        val friendDTO = ServerApi.addFriend(request).friend
        db.friendsDao().insert(friendDTO.toDB())
        return Friend.fromDTO(friendDTO)
    }

    suspend fun getFriends(sync: Boolean = false): List<Friend> {
        return if (sync) {
            val friends = ServerApi.getFriends().friends
            val friendsDB = friends.map { it.toDB() }
            db.friendsDao().insert(friendsDB)
            friends.map { Friend.fromDTO(it) }
        } else {
            val friendsDb = db.friendsDao().getFriends()
            if (friendsDb.isEmpty())
                getFriends(true)
            else friendsDb.map { it.toFriend() }
        }
    }

    suspend fun updateUser(user: Person) {
        val dto = PersonDTO.fromPerson(user)
        return ServerApi.updateUser(UpdateUser.Request(dto))
    }
}