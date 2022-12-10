package com.mikkelthygesen.billsplit.data.network

import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.data.network.dto.EventDTO
import com.mikkelthygesen.billsplit.data.network.dto.FriendStatusDTO
import com.mikkelthygesen.billsplit.data.network.requests.AddFriend
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

object KtorClient {

    private val module = SerializersModule {
        polymorphic(EventDTO::class) {
            subclass(EventDTO.ExpenseDTO::class, EventDTO.ExpenseDTO.serializer())
            subclass(EventDTO.ChangeDTO::class, EventDTO.ChangeDTO.serializer())
            subclass(EventDTO.PaymentDTO::class, EventDTO.PaymentDTO.serializer())
        }
        polymorphic(FriendStatusDTO::class) {
            subclass(
                FriendStatusDTO.RequestSent::class,
                FriendStatusDTO.RequestSent.serializer()
            )
            subclass(
                FriendStatusDTO.RequestAccepted::class,
                FriendStatusDTO.RequestAccepted.serializer()
            )
        }
        polymorphic(AddFriend.Request::class) {
            subclass(AddFriend.Request.UserId::class, AddFriend.Request.UserId.serializer())
            subclass(AddFriend.Request.Email::class, AddFriend.Request.Email.serializer())
        }
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        serializersModule = module
        encodeDefaults = true
        prettyPrint = true
    }

    val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout) {
            this.connectTimeoutMillis = 20 * 1000
            this.requestTimeoutMillis = 20 * 1000
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        defaultRequest {
            url(BuildConfig.HOST_NAME)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }
}



