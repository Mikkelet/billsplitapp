package com.mikkelthygesen.billsplit

import android.app.Application
import com.google.firebase.FirebaseApp
import com.mikkelthygesen.billsplit.data.auth.authProvider
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.sql.Time

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        authProvider.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        authProvider.onDestroy()
    }
}