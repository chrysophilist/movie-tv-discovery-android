package com.prince.cinemon.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prince.cinemon.data.local.prefs.OnboardingDataStore
import com.prince.cinemon.domain.model.AppStartDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RootViewModel (
    private val onboardingDataStore: OnboardingDataStore
): ViewModel() {

    private val _startDestination = MutableStateFlow<AppStartDestination?>(null)
    val startDestination: StateFlow<AppStartDestination?> = _startDestination

    init {
        setStartDestination()
    }


    private fun setStartDestination() {
        viewModelScope.launch {
            val onboardingCompleted = onboardingDataStore.completed.first()

            _startDestination.value =
                if (onboardingCompleted) {
                    AppStartDestination.Home
                } else {
                    AppStartDestination.Onboarding
                }
        }
    }
}