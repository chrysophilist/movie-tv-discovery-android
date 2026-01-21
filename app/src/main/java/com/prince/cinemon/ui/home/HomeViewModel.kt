package com.prince.cinemon.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prince.cinemon.domain.model.HomeContent
import com.prince.cinemon.domain.repository.Repository
import com.prince.cinemon.domain.util.ApiKeyProvider
import com.prince.cinemon.domain.util.AppError
import com.prince.cinemon.domain.util.Result
import com.prince.cinemon.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: Repository,
    private val apiKeyProvider: ApiKeyProvider
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeContent>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeContent>> = _uiState

    private var hasLoaded = false
    private val _isApiKeyMissing = MutableStateFlow(false)
    val isApiKeyWasMissing:StateFlow<Boolean> = _isApiKeyMissing.asStateFlow()

    private val _isQuotaExhausted = MutableStateFlow(false)
    val isQuotaExhausted: StateFlow<Boolean> = _isQuotaExhausted

    init {
        loadHome()
    }

    fun loadHome(force: Boolean = false) {
        viewModelScope.launch {

            val hasApiKey = apiKeyProvider.hasApiKey()
            if (!hasApiKey) {
                _isApiKeyMissing.value = true
                return@launch
            } else {
                _isApiKeyMissing.value = false
            }

            // Prevent unnecessary reload
            if (hasLoaded && !force) return@launch


            _uiState.value = UiState.Loading
            when(val result = repo.fetchHomeContent()) {
                is Result.Success -> {
                    hasLoaded = true
                    _isApiKeyMissing.value = false
                    _isQuotaExhausted.value = false
                    _uiState.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    when(result.error) {
                        is AppError.MissingApiKey -> _isApiKeyMissing.value = true
                        is AppError.QuotaExceeded -> _isQuotaExhausted.value = true
                        else -> null
                    }
                    _uiState.value = UiState.Error(result.error)
                }
            }
        }
    }
}