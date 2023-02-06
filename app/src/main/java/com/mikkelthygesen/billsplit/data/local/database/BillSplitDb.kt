package com.mikkelthygesen.billsplit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikkelthygesen.billsplit.data.local.database.converters.Converters
import com.mikkelthygesen.billsplit.data.local.database.daos.EventsDao
import com.mikkelthygesen.billsplit.data.local.database.daos.FriendsDao
import com.mikkelthygesen.billsplit.data.local.database.daos.GroupsDao
import com.mikkelthygesen.billsplit.data.local.database.daos.ServicesDao
import com.mikkelthygesen.billsplit.data.local.database.model.*

@Database(
    entities = [
        FriendDb::class,
        GroupDb::class,
        GroupExpenseDb::class,
        PaymentDb::class,
        SubscriptionServiceDb::class,
        ExpenseChangeDb::class],
    version = 11,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class BillSplitDb : RoomDatabase() {

    abstract fun friendsDao(): FriendsDao
    abstract fun groupsDao(): GroupsDao
    abstract fun eventsDao(): EventsDao
    abstract fun servicesDao(): ServicesDao
}