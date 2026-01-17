package com.prince.movietvdiscovery.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prince.movietvdiscovery.domain.model.HomeContent
import com.prince.movietvdiscovery.domain.repository.Repository
import com.prince.movietvdiscovery.domain.util.Result
import com.prince.movietvdiscovery.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: Repository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeContent>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeContent>> = _uiState

    private var hasLoaded = true

    init {
        loadHome()
    }

    fun loadHome(force: Boolean = false) {

        if (_uiState.value is UiState.Success && !force) return

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            when(val result = repo.fetchHomeContent()) {
                is Result.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.error)
                }
            }
        }
    }
}