package com.mikkelthygesen.billsplit.data.network

import com.mikkelthygesen.billsplit.data.network.dto.AddEventRequestDTO
import com.mikkelthygesen.billsplit.data.network.dto.AddGroupRequestDTO
import com.mikkelthygesen.billsplit.data.network.dto.GetGroupRequestDTO
import com.mikkelthygesen.billsplit.data.network.dto.GetGroupResponseDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface ServerApi {

    @POST("addGroup")
    suspend fun addGroup(@Body body: AddGroupRequestDTO)

    @POST("addEvent")
    suspend fun addEvent(@Body body: AddEventRequestDTO)

    @POST("getGroup")
    suspend fun getGroup(@Body body: GetGroupRequestDTO) : GetGroupResponseDTO
}