package com.mikkelthygesen.billsplit.data.network

import com.mikkelthygesen.billsplit.data.network.requests.*
import io.ktor.client.call.*
import io.ktor.client.request.*

object ServerApi {

    suspend fun addEvent(body: AddEvent.Request): AddEvent.Response {
        return KtorClient.client.post("addEvent") {
            setBody(body)
        }.body()
    }

    suspend fun getGroup(body: GetGroup.Request): GetGroup.Response {
        return KtorClient.client.post("getGroup") {
            setBody(body)
        }.body()
    }

    suspend fun getGroups(): GetGroups.Response {
        return KtorClient.client.post("getGroups").body()
    }

    suspend fun addGroup(body: AddGroup.Request): AddGroup.Response {
        return KtorClient.client.post("addGroup") {
            setBody(body)
        }.body()
    }

    suspend fun addFriend(body: AddFriend.Request): AddFriend.Response {
        return KtorClient.client.post("addFriend") {
            setBody(body)
        }.body()
    }

    suspend fun getFriends(): GetFriends.Response {
        return KtorClient.client.post("getFriends").body()
    }

    suspend fun updateUser(updateUserRequest: UpdateUser.Request) {
        return KtorClient.client.post("updateUser") {
            setBody(updateUserRequest)
        }.body()
    }
}