package com.mikkelthygesen.billsplit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikkelthygesen.billsplit.data.local.database.converters.Converters
import com.mikkelthygesen.billsplit.data.local.database.daos.*
import com.mikkelthygesen.billsplit.data.local.database.model.*

@Database(
    entities = [
        FriendDb::class,
        GroupDb::class,
        GroupExpenseDb::class,
        PaymentDb::class,
        SubscriptionServiceDb::class,
        ExpenseChangeDb::class],
    version = 12,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class BillSplitDb : RoomDatabase() {

    abstract fun friendsDao(): FriendsDao
    abstract fun groupsDao(): GroupsDao
    abstract fun servicesDao(): ServicesDao
    abstract fun expenseChangesDao() : ExpenseChangeDao
    abstract fun paymentsDao() : PaymentEventDao
    abstract fun groupExpensesDao() : GroupExpenseEventDao
}