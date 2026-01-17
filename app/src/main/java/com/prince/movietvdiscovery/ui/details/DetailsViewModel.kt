package com.prince.movietvdiscovery.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prince.movietvdiscovery.domain.model.TitleDetailsWithSources
import com.prince.movietvdiscovery.domain.repository.Repository
import com.prince.movietvdiscovery.domain.util.Result
import com.prince.movietvdiscovery.ui.common.UiState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel (
    private val repo: Repository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<TitleDetailsWithSources>>(UiState.Loading)
    val uiState: StateFlow<UiState<TitleDetailsWithSources>> = _uiState

    private var loadTitleId: Int? = null

    fun loadDetails(titleId: Int) {

        if (loadTitleId == titleId &&
            _uiState.value is UiState.Success) return

        loadTitleId = titleId

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            when(val result = repo.fetchTitleDetailsWithSources(titleId) ) {
                is Result.Success -> {
                    _uiState.value = UiState.Success(
                        result.data
                    )
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.error)
                }
            }
        }
    }
}