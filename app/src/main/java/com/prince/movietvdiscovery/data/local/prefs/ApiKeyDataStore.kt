package com.prince.movietvdiscovery.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private const val DATASTORE_NAME = "api_key_prefs"

private val Context.dataStore by preferencesDataStore(
    name = DATASTORE_NAME
)


class ApiKeyDataStore (
    private val context: Context
) {

    private val API_KEY = stringPreferencesKey("watchmode_api_key")

    val apiKey: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[API_KEY]
        }

    suspend fun saveApiKey(apiKey: String) {
        context.dataStore.edit { prefs ->
            prefs[API_KEY] = apiKey
        }
    }

    suspend fun clearApiKey() {
        context.dataStore.edit { prefs ->
            prefs.remove(API_KEY)
        }
    }
}