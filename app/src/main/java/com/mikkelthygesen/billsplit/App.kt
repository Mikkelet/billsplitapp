package com.mikkelthygesen.billsplit

import android.app.Application
import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var authProvider: AuthProvider

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