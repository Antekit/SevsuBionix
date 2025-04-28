package com.example.myapplication

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val Ip_adress = stringPreferencesKey("IP")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveIp(ip_adress: String) {
        dataStore.edit { preferences ->
            preferences[Ip_adress] = ip_adress
        }
    }

    val ip_Adress: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
            .map { preferences ->
        preferences[Ip_adress] ?: "Kek"
    }


}