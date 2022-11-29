package com.mikkelthygesen.billsplit.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.data.network.dto.EventDTO
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import retrofit2.Retrofit


@ExperimentalSerializationApi
object Retrofit {

    private val BASE_URL = if(BuildConfig.DEBUG) "http://10.0.2.2:5000/billsplittapp/us-central1/" else "https://us-central1-billsplittapp.cloudfunctions.net/"
    private val BASE_URL_PROD = "https://us-central1-billsplittapp.cloudfunctions.net/"

    val serverApi: ServerApi = getRetrofit(BASE_URL).create(ServerApi::class.java)

    private fun getRetrofit(withBaseUrl: String): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(withBaseUrl)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getHttpLoggingInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return loggingInterceptor
    }
}