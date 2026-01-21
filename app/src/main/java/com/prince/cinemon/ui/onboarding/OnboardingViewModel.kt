package com.prince.cinemon.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prince.cinemon.data.local.prefs.OnboardingDataStore
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