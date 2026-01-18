package com.prince.movietvdiscovery.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prince.movietvdiscovery.data.local.prefs.OnboardingDataStore
import kotlinx.coroutines.launch

class OnboardingViewModel (
    private val onboardingDataStore: OnboardingDataStore
): ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            onboardingDataStore.setOnboardingCompleted()
        }
    }
}