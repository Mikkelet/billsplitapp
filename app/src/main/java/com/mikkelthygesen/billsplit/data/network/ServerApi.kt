package com.mikkelthygesen.billsplit.data.network

import com.mikkelthygesen.billsplit.data.network.dto.*
import retrofit2.http.Body
import retrofit2.http.POST

interface ServerApi {

    @POST("addGroup")
    suspend fun addGroup(@Body body: AddGroupRequestDTO) : GroupDTO

    @POST("addEvent")
    suspend fun addEvent(@Body body: AddEventRequestDTO) : EventDTO

    @POST("getGroup")
    suspend fun getGroup(@Body body: GetGroupRequestDTO) : GetGroupResponseDTO
}