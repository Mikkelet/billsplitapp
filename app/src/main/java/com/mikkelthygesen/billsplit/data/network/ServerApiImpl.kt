package com.mikkelthygesen.billsplit.data.network

import androidx.compose.ui.res.dimensionResource
import com.mikkelthygesen.billsplit.data.network.dto.*
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.models.interfaces.Event
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel

@Suppress("OPT_IN_USAGE")
class ServerApiImpl {

    suspend fun addGroup(group: Group): Group {
        val groupDTO = GroupDTO.fromGroup(group)
        val addGroupDTO = AddGroupRequestDTO(groupDTO)
        return ServerApi.addGroup(addGroupDTO).toGroup()
    }

    suspend fun addEvent(groupId: String, event: Event): EventDTO {
        val eventDto: EventDTO = when (event) {
            is GroupExpense -> EventDTO.fromExpense(event)
            is Payment -> EventDTO.fromPayment(event)
            is GroupExpensesChanged -> EventDTO.fromChange(event)
            else -> throw Exception("Invalid event")
        }
        return ServerApi.addEvent(AddEventRequestDTO(groupId, eventDto))
    }

    suspend fun getGroup(groupId: String): Group {
        val dto = ServerApi.getGroup(GetGroupRequestDTO(groupId))
        return dto.toGroup()
    }

    suspend fun getGroups(userId: String): List<Group> {
        val dtos = ServerApi.getGroups(GetGroupsDTO.Request(userId))
        return dtos.groups.map { it.toGroup() }
    }

    suspend fun addFriend(userId: String): AddFriendDTO.Response {
        val addFriendRequestDTO = AddFriendDTO.Request(
            MainViewModel.getLoggedIn().uid,
            userId,
            System.currentTimeMillis()
        )
        return ServerApi.addFriend(addFriendRequestDTO)
    }
}