package com.prince.movietvdiscovery.ui.home

import androidx.lifecycle.ViewModel
import com.prince.movietvdiscovery.domain.model.HomeContent
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

class HomeViewModel(
    private val repo: Repository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeContent>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeContent>> = _uiState

    private val disposables = CompositeDisposable()

    init {
        fetchHomeContent()
    }

    fun fetchHomeContent(){

        repo.fetchHomeContent()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _uiState.value = UiState.Loading
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { result ->
                    when(result) {
                        is Result.Success -> {
                            _uiState.value = UiState.Success(result.data)
                        }
                        is Result.Error -> {
                            _uiState.value = UiState.Error(result.error)
                        }
                    }
                },
//                onError = { error ->
//                    _uiState.value = UiState.Error(error.message ?: "Unknown Error")
//                }
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.clear()
    }
}