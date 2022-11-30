package com.mikkelthygesen.billsplit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.sql.Time

@HiltAndroidApp
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}