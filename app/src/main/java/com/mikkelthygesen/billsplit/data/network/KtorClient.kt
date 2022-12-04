package com.mikkelthygesen.billsplit.data.network

import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.data.network.dto.AddFriendDTO
import com.mikkelthygesen.billsplit.data.network.dto.EventDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import timber.log.Timber

object KtorClient {

    private val module = SerializersModule {
        polymorphic(EventDTO::class) {
            subclass(EventDTO.ExpenseDTO::class, EventDTO.ExpenseDTO.serializer())
            subclass(EventDTO.ChangeDTO::class, EventDTO.ChangeDTO.serializer())
            subclass(EventDTO.PaymentDTO::class, EventDTO.PaymentDTO.serializer())
        }
        polymorphic(AddFriendDTO.Response::class) {
            subclass(
                AddFriendDTO.Response.AlreadyRequested::class,
                AddFriendDTO.Response.AlreadyRequested.serializer()
            )
            subclass(
                AddFriendDTO.Response.RequestSent::class,
                AddFriendDTO.Response.RequestSent.serializer()
            )
            subclass(
                AddFriendDTO.Response.RequestAccepted::class,
                AddFriendDTO.Response.RequestAccepted.serializer()
            )
        }
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        serializersModule = module
        encodeDefaults = true
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
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        defaultRequest {
            url(BuildConfig.HOST_NAME)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }.apply {
        plugin(HttpSend).intercept { req ->
            println("NETW --> ${req.method.value}(${req.url.build()})")
            Timber.i("NETW --- ${req.body}")
            val call = execute(req)
            val response = call.response
            val durationMillis = response.responseTime.timestamp - response.requestTime.timestamp
            println("NETW <-- [${response.body<Any>()}] ($durationMillis ms)")
            call
        }
    }
}

