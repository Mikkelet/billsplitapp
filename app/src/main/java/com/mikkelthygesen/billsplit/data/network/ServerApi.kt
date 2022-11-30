package com.mikkelthygesen.billsplit.data.network

import com.mikkelthygesen.billsplit.data.network.dto.*
import io.ktor.client.call.*
import io.ktor.client.request.*

object ServerApi {

    suspend fun addEvent(addEventRequestDTO: AddEventRequestDTO): EventDTO {
        return KtorClient.client.post("addEvent") {
            setBody(addEventRequestDTO)
        }.body()
    }

    suspend fun getGroup(getGroupRequestDTO: GetGroupRequestDTO): GetGroupResponseDTO {
        return KtorClient.client.post("getGroup") {
            setBody(getGroupRequestDTO)
        }.body()
    }

    suspend fun addGroup(addGroupRequestDTO: AddGroupRequestDTO): GroupDTO {
        val response = KtorClient.client.post("addGroup") {
            setBody(addGroupRequestDTO)
        }
        println("qqq add group response=$response")
        val body = response.body<GroupDTO>()
        println("qqq add group $body")
        return body
    }
}