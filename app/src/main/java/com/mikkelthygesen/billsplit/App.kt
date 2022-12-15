package com.mikkelthygesen.billsplit

import android.app.Application
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.mikkelthygesen.billsplit.data.auth.authProvider
import com.mikkelthygesen.billsplit.data.local.room.BillSplitDb
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.sql.Time

lateinit var db: BillSplitDb

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        authProvider.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            BillSplitDb::class.java, "splittsby-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onTerminate() {
        super.onTerminate()
        authProvider.onDestroy()
    }
}