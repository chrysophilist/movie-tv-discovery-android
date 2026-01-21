package com.prince.cinemon
import com.prince.cinemon.data.remote.api.WatchmodeApi
import com.prince.cinemon.data.remote.dto.ListTitlesResponseDto
import com.prince.cinemon.data.repository.RepositoryImpl
import com.prince.cinemon.domain.util.AppError
import com.prince.cinemon.domain.util.Result
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RepositoryImplTest {

    @get:Rule
    val rxRule = RxImmediateSchedulerRule()

    private lateinit var repository: RepositoryImpl
    private val api: WatchmodeApi = mockk()

    @Before
    fun setup() {
        repository = RepositoryImpl(api)
    }

    @Test // TC-02: Movies API Failure
    fun fetchHomeContent_returnsError_whenMoviesApiFails() {
        // Given: Movies API fails
        every { api.getMovies() } returns
                Single.error(RuntimeException("Movies API failure"))

        // Given: TV Shows API succeeds
        every { api.getTvShows() } returns
                Single.just(
                    ListTitlesResponseDto(
                        titles = listOf(mockk())
                    )
                )

        // When
        val result = repository.fetchHomeContent().blockingGet()

        // Then
        assert(result is Result.Error)
        assert((result as Result.Error).error is AppError.Unknown)
    }
}
