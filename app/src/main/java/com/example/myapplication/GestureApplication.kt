package com.example.myapplication

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


private const val IP_ADRESS = "ip_adress"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = IP_ADRESS
)

class GestureApplication:Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository


    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}