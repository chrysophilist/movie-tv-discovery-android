package com.prince.movietvdiscovery.ui.apikey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prince.movietvdiscovery.domain.model.ApiKeyStatus
import com.prince.movietvdiscovery.domain.repository.Repository
import com.prince.movietvdiscovery.domain.util.ApiKeyProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ApiKeyViewModel (
    private val repo: Repository,
    private val apiKeyProvider: ApiKeyProvider
): ViewModel() {

    private val _uiState = MutableStateFlow(ApiKeyUiState())
    val uiState: StateFlow<ApiKeyUiState> = _uiState.asStateFlow()

    init {
        observeApiKey()
    }

    private fun observeApiKey() {

        viewModelScope.launch {
            apiKeyProvider.apiKeyFlow.collect { key ->
                _uiState.value = _uiState.value.copy(
                    currentKey = key,
                    status = if (key.isNullOrBlank()){
                        ApiKeyStatus.Missing
                    } else {
                        _uiState.value.status
                    }
                )
            }
        }
    }

    fun validateKey(){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            val result = repo.validateApiKey()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                status = result
            )
        }
    }

    fun saveApiKey(key: String) {
        viewModelScope.launch {
            apiKeyProvider.saveApiKey(key)

            _uiState.value = _uiState.value.copy(
                currentKey = key,
                status = ApiKeyStatus.Missing
            )
        }
    }

    fun clearApiKey() {
        viewModelScope.launch {
            apiKeyProvider.clearApiKey()
            _uiState.value = ApiKeyUiState()
        }
    }
}