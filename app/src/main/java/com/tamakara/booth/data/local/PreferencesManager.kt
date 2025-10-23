package com.tamakara.booth.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "booth_prefs")

class PreferencesManager(private val context: Context) {

    companion object {
        private val USER_ID_KEY = longPreferencesKey("user_id")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val PHONE_KEY = stringPreferencesKey("phone")
    }

    val userId: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[USER_ID_KEY] ?: -1L
    }

    val token: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[TOKEN_KEY] ?: ""
    }

    val phone: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PHONE_KEY] ?: ""
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun saveUserInfo(userId: Long, token: String, phone: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = userId
            prefs[TOKEN_KEY] = token
            prefs[PHONE_KEY] = phone
        }
    }

    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
