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
        return client.post("event") {
            setBody(body)
        }.body()
    }

    suspend fun getGroup(params: GetGroup.Request): GetGroup.Response {
        return client.get("group/${params.groupId}").body()
    }

    suspend fun getGroups(): GetGroups.Response {
        return client.get("groups").body()
    }

    suspend fun addGroup(body: AddGroup.Request): AddGroup.Response {
        return client.post("group") {
            setBody(body)
        }.body()
    }

    suspend fun addFriend(body: AddFriend.Request): AddFriend.Response {
        return client.post("friends") {
            setBody(body)
        }.body()
    }

    suspend fun getFriends(): GetFriends.Response {
        return client.get("friends").body()
    }

    suspend fun updateUser(updateUserRequest: UpdateUser.Request) {
        return client.post("updateUser") {
            setBody(updateUserRequest)
        }.body()
    }

    suspend fun addSubscriptionService(addServiceRequest: AddSubscriptionService.Request): AddSubscriptionService.Response {
        return client.post("service") {
            setBody(addServiceRequest)
        }.body()
    }
}