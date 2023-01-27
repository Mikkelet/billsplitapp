package com.mikkelthygesen.billsplit.core.di

import com.mikkelthygesen.billsplit.data.remote.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {

    @Singleton
    @Provides
    fun bindKtorClient(
        ktorClient: KtorClient
    ): HttpClient = ktorClient.client
}

