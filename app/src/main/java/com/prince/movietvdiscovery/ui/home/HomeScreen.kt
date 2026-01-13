package com.prince.movietvdiscovery.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prince.movietvdiscovery.domain.model.HomeContent
import com.prince.movietvdiscovery.ui.common.UiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onClick: (Int)-> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        onClick = onClick
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    uiState: UiState<HomeContent>,
    onClick: (Int) -> Unit
) {
    val tabs = listOf("Movies", "TV Shows")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (uiState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(uiState.message)
                    }
                }

                is UiState.Success -> {
                    val content = uiState.data

                    when (selectedTabIndex) {
                        0 -> TitleList(
                            titles = content.movies.map { movie ->
                                TitleUiModel(
                                    id = movie.id,
                                    title = movie.title
                                )
                            },
                            onClick = onClick
                        )
                        1 -> TitleList(
                            titles = content.tvShows.map { tvshow ->
                                TitleUiModel(
                                    id = tvshow.id,
                                    title = tvshow.title
                                )
                            },
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun TitleList(
    titles: List<TitleUiModel>,
    onClick: (Int)-> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(titles) { item ->
            ItemCard(
                title = item.title,
                onClick = { onClick(item.id) }
            )
        }
    }
}

@Composable
fun ItemCard(
    title: String,
    onClick: ()-> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
data class TitleUiModel(
    val id: Int,
    val title: String
)
