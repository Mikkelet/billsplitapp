package com.mikkelthygesen.billsplit.data.remote

import com.mikkelthygesen.billsplit.data.remote.requests.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerApi @Inject constructor(
    private val client: HttpClient
) {

    suspend fun addEvent(body: AddEvent.Request): AddEvent.Response {
        return client.post("addEvent") {
            setBody(body)
        }.body()
    }

    suspend fun getGroup(body: GetGroup.Request): GetGroup.Response {
        return client.post("getGroup") {
            setBody(body)
        }.body()
    }

    suspend fun getGroups(): GetGroups.Response {
        return client.post("getGroups").body()
    }

    suspend fun addGroup(body: AddGroup.Request): AddGroup.Response {
        return client.post("addGroup") {
            setBody(body)
        }.body()
    }

    suspend fun addFriend(body: AddFriend.Request): AddFriend.Response {
        return client.post("addFriend") {
            setBody(body)
        }.body()
    }

    suspend fun getFriends(): GetFriends.Response {
        return client.post("getFriends").body()
    }

    suspend fun updateUser(updateUserRequest: UpdateUser.Request) {
        return client.post("updateUser") {
            setBody(updateUserRequest)
        }.body()
    }
}