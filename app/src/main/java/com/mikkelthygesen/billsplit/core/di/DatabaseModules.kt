package com.mikkelthygesen.billsplit.core.di

import android.content.Context
import androidx.room.Room
import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModules {

    @Provides
    @Singleton
    fun bindDatabase(@ApplicationContext context: Context) : BillSplitDb = Room.databaseBuilder(
        context,
        BillSplitDb::class.java, "splittsby-db"
    )
        .fallbackToDestructiveMigration()
        .build()
}