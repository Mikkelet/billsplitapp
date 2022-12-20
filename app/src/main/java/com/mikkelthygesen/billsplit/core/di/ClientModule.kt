package com.mikkelthygesen.billsplit.core.di

import com.mikkelthygesen.billsplit.data.remote.KtorClient
import com.mikkelthygesen.billsplit.data.remote.ServerApi
import com.mikkelthygesen.billsplit.data.remote.ServerApiImpl
import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {

    @Singleton
    @Provides
    fun bindKtorClient(ktorClient: KtorClient): HttpClient = ktorClient.client
}

