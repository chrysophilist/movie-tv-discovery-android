package com.prince.cinemon

import com.prince.cinemon.domain.model.HomeContent
import com.prince.cinemon.domain.repository.Repository
import com.prince.cinemon.domain.util.AppError
import com.prince.cinemon.domain.util.Result
import com.prince.cinemon.ui.common.UiState
import com.prince.cinemon.ui.home.HomeViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val rxRule = RxImmediateSchedulerRule()

    private lateinit var viewModel: HomeViewModel
    private val repository: Repository = mockk()


    @Before
    fun setup() {
        every { repository.fetchHomeContent() } returns
                Single.just(
                    Result.Success(
                        HomeContent(
                            movies = emptyList(),
                            tvShows = emptyList()
                        )
                    )
                )
        viewModel = HomeViewModel(repository)
    }


    @Test // TC-01: Successful API Response
    fun fetchHomeContent_emitsSuccess_whenRepositoryReturnsData() {

        // Given
        val homeContent = HomeContent(
            movies = listOf(mockk()),
            tvShows = listOf(mockk())
        )

        every { repository.fetchHomeContent() } returns
                Single.just(Result.Success(homeContent))

        // When
        viewModel.fetchHomeContent()

        // Then
        val state = viewModel.uiState.value
        assert(state is UiState.Success)
    }


    @Test // TC-03: Empty API Response
    fun fetchHomeContent_emitsSuccess_whenResponseIsEmpty() {

        // Given
        val homeContent = HomeContent(
            movies = emptyList(),
            tvShows = emptyList()
        )

        every { repository.fetchHomeContent() } returns
                Single.just(Result.Success(homeContent))

        // When
        viewModel.fetchHomeContent()

        // Then
        val state = viewModel.uiState.value
        assert(state is UiState.Success)
    }


    @Test // TC-04: Network Timeout
    fun fetchHomeContent_emitsTimeoutError() {

        // Given
        val error = AppError.Timeout()

        every { repository.fetchHomeContent() } returns
                Single.just(Result.Error(error))

        // When
        viewModel.fetchHomeContent()

        // Then
        val state = viewModel.uiState.value
        assert(((state as UiState.Error).message) is AppError.Timeout)
    }
}