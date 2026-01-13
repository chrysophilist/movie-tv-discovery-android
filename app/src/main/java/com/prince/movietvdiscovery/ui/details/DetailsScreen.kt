package com.prince.movietvdiscovery.ui.details

import android.widget.Space
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.prince.movietvdiscovery.ui.common.DetailsUiState
import com.prince.movietvdiscovery.ui.common.UiState
import org.jetbrains.annotations.Async
import org.koin.androidx.compose.koinViewModel


@Composable
fun DetailsScreen(
    titleId: Int,
    viewModel: DetailsViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(titleId) {
        viewModel.loadDetails(titleId)
    }

    DetailsScreenContent(uiState)
}

@Composable
private fun DetailsScreenContent(
    uiState: UiState<DetailsUiState>
) {

    when(uiState){

        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(uiState.message)
            }
        }

        is UiState.Success -> {

            val details = uiState.data

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                item {

                    AsyncImage(
                        model = details.details.posterUrl,
                        contentDescription = details.details.title,
                        modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = details.details.title
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = details.details.description ?: "Description not available"
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = details.details.releaseDate ?: "NA"
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}