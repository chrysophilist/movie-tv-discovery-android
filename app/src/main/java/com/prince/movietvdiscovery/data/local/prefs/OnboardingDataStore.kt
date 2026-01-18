package com.prince.movietvdiscovery.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("onboarding_prefs")

class OnboardingDataStore (
    private val context: Context
) {

    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

    val completed: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[ONBOARDING_COMPLETED] ?: false
        }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED] = true
        }
    }
}