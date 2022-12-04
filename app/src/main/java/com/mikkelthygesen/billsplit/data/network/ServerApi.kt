package com.mikkelthygesen.billsplit.data.network

import com.mikkelthygesen.billsplit.data.network.dto.*
import io.ktor.client.call.*
import io.ktor.client.request.*

object ServerApi {

    suspend fun addEvent(body: AddEventRequestDTO): EventDTO {
        return KtorClient.client.post("addEvent") {
            setBody(body)
        }.body()
    }

    suspend fun getGroup(body: GetGroupRequestDTO): GetGroupResponseDTO {
        return KtorClient.client.post("getGroup") {
            setBody(body)
        }.body()
    }

    suspend fun getGroups(body: GetGroupsDTO.Request): GetGroupsDTO.Response {
        return KtorClient.client.post("getGroups") {
            setBody(body)
        }.body()
    }

    suspend fun addGroup(body: AddGroupRequestDTO): GroupDTO {
        return KtorClient.client.post("addGroup") {
            setBody(body)
        }.body()
    }

    suspend fun addFriend(addFriendRequestDTO: AddFriendDTO.Request): AddFriendDTO.Response {
        return KtorClient.client.post("addFriend") {
            setBody(addFriendRequestDTO)
        }.body()
    }
}