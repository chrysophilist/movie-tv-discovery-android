package com.prince.movietvdiscovery.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prince.movietvdiscovery.domain.model.HomeContent
import com.prince.movietvdiscovery.domain.repository.Repository
import com.prince.movietvdiscovery.domain.util.ApiKeyProvider
import com.prince.movietvdiscovery.domain.util.Result
import com.prince.movietvdiscovery.ui.common.UiState
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
                    _uiState.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.error)
                }
            }
        }
    }
}