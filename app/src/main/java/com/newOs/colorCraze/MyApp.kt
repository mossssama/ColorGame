package com.newOs.colorCraze

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp :Application(){

    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}