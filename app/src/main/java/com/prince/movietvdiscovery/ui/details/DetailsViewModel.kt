package com.prince.movietvdiscovery.ui.details

import androidx.lifecycle.ViewModel
import com.prince.movietvdiscovery.domain.repository.Repository
import com.prince.movietvdiscovery.ui.common.DetailsUiState
import com.prince.movietvdiscovery.ui.common.UiState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailsViewModel (
    private val repo: Repository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<DetailsUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<DetailsUiState>> = _uiState

    private val disposables = CompositeDisposable()
    private var loadTitleId: Int? = null

    fun loadDetails(titleId: Int) {

        if (loadTitleId == titleId &&
            _uiState.value is UiState.Success) return

        loadTitleId = titleId

        repo.fetchTitleDetails(titleId)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _uiState.value = UiState.Loading
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { details ->
                    _uiState.value = UiState.Success(
                        DetailsUiState(details)
                    )
                },
                onError = { error ->
                    _uiState.value = UiState.Error(
                        error.message ?: "Unknown Error"
                    )
                }
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.clear()
    }
}