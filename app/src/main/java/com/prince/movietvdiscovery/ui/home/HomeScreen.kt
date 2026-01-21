package com.prince.movietvdiscovery.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prince.movietvdiscovery.domain.model.HomeContent
import com.prince.movietvdiscovery.domain.util.AppError
import com.prince.movietvdiscovery.ui.common.RetrySection
import com.prince.movietvdiscovery.ui.common.UiState
import com.prince.movietvdiscovery.ui.navigation.AppScaffoldState
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onClick: (Int)-> Unit,
    setScaffoldState: (AppScaffoldState)-> Unit,
    showSnackbar: suspend (String)-> Unit,
    onNavigateToSettings: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isApiKeyMissing by viewModel.isApiKeyWasMissing.collectAsStateWithLifecycle()
    val isQuotaExceeded by viewModel.isQuotaExhausted.collectAsStateWithLifecycle()

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        setScaffoldState(
            AppScaffoldState(
                topBar = {
                    TopAppBar(
                        title = {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Streamy",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFd91f25)
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = onOpenSettings,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(Icons.Default.Settings, "Settings")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent
                        )
                    )
                }
            )
        )
        viewModel.loadHome()
    }


    when {
        isApiKeyMissing -> HomeEmptyStateScreen(onAddApiKey = onNavigateToSettings)

        isQuotaExceeded -> HomeQuotaExceededScreen(
            onOpenApiKeySettings = onNavigateToSettings,
            onNavigateToWatchmode = { uriHandler.openUri("https://api.watchmode.com/requestApiKey/") }
        )

        else -> HomeScreenContent(
            uiState = uiState,
            onClick = onClick,
            onRetry = { viewModel.loadHome(force = true) },
            showSnackbar = showSnackbar
        )
    }

    LaunchedEffect(isApiKeyMissing) {
        if ( isApiKeyMissing ) {
            showSnackbar("API key added. Tap Retry to load content.")
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    uiState: UiState<HomeContent>,
    onClick: (Int) -> Unit,
    onRetry: ()-> Unit,
    showSnackbar: suspend (String) -> Unit
) {

    var showLoadingText by remember { mutableStateOf(false) }
    var showSlowNetworkHint by remember { mutableStateOf(false) }
    LaunchedEffect(uiState is UiState.Loading) {
        if (uiState is UiState.Loading) {
            showLoadingText = false
            showSlowNetworkHint = false

            delay(2_000)
            showLoadingText = true

            delay(3_000)
            showSlowNetworkHint = true
        } else {
            showLoadingText = false
            showSlowNetworkHint = false
        }
    }


    val tabs = listOf("Movies", "TV Shows")
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    modifier = Modifier.fillMaxSize()
                ) {

                    HomeScreenShimmer()

                    if (showLoadingText || showSlowNetworkHint) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (showLoadingText) {
                                Text(
                                    text = "Loading content",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (showSlowNetworkHint) {
                                Text(
                                    text = "This may take a bit longer on slow networks",
                                    modifier = Modifier.padding(top = 4.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            is UiState.Error -> {
                val error = uiState.message

                when(error) {

                    is AppError.Network -> {
                        LaunchedEffect(error) {
                            showSnackbar(error.message)
                        }
                        RetrySection(error.message, onRetry)
                    }

                    is AppError.Timeout -> {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                        RetrySection(error.message, onRetry)
                    }

                    else -> {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                        RetrySection(error.message, onRetry)
                    }
                }
            }

            is UiState.Success -> {
                val content = uiState.data

                when (selectedTabIndex) {
                    0 -> TitleList(
                        titles = content.movies.map { movie ->
                            TitleUiModel(
                                id = movie.id,
                                title = movie.title,
                                year = movie.year
                            )
                        },
                        onClick = onClick
                    )
                    1 -> TitleList(
                        titles = content.tvShows.map { tvshow ->
                            TitleUiModel(
                                id = tvshow.id,
                                title = tvshow.title,
                                year = tvshow.year
                            )
                        },
                        onClick = onClick
                    )
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
                title = "${item.title} (${item.year})",
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
    val title: String,
    val year: Int?
)
