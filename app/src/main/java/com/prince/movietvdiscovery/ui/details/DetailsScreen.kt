package com.prince.movietvdiscovery.ui.details

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.prince.movietvdiscovery.domain.model.TitleDetailsWithSources
import com.prince.movietvdiscovery.domain.util.AppError
import com.prince.movietvdiscovery.ui.common.RetrySection
import com.prince.movietvdiscovery.ui.common.UiState
import com.prince.movietvdiscovery.ui.navigation.AppScaffoldState
import org.koin.androidx.compose.koinViewModel
import androidx.core.net.toUri
import com.prince.movietvdiscovery.domain.model.StreamingType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    titleId: Int,
    viewModel: DetailsViewModel = koinViewModel(),
    setScaffoldState: (AppScaffoldState)-> Unit,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(titleId) {
        viewModel.loadDetails(titleId)
    }

    LaunchedEffect(uiState) {
        setScaffoldState(
            AppScaffoldState(
                containerColor = Color.Transparent,
                applyContentPadding = false,
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(
                                onClick = onBack
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back to Home"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent
                        )
                    )
                },
                floatingActionButton = {
                    if (uiState is UiState.Success) {

                        val sources = (uiState as UiState.Success<TitleDetailsWithSources>).data.sources
                        val bestSource = pickBestSource(sources)

                        ExtendedFloatingActionButton(
                            onClick = {
                                val url = when {
                                    bestSource?.androidUrl.isValidUrl() -> bestSource?.androidUrl
                                    bestSource?.webUrl.isValidUrl() -> bestSource?.webUrl
                                    else -> null

                                }

                                if(url == null){
                                    Toast.makeText(context, "Streaming link not available", Toast.LENGTH_SHORT).show()
                                    return@ExtendedFloatingActionButton
                                }

                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                            containerColor = Color.Red

                        ) {
                            Text(
                                text = when(bestSource?.type){
                                    StreamingType.FREE -> "Watch Free"
                                    StreamingType.SUBSCRIPTION -> "Watch Now"
                                    StreamingType.RENT -> "Rent"
                                    StreamingType.BUY -> "Buy"
                                    StreamingType.TV -> "Watch on TV"
                                    else -> "Watch"
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                },
                fabPosition = FabPosition.Center,
                bottomBar = null
            )
        )
    }


    DetailsScreenContent(
        uiState = uiState,
        onRetry = { viewModel.loadDetails(titleId) }

    )
}

@Composable
private fun DetailsScreenContent(
    uiState: UiState<TitleDetailsWithSources>,
    onRetry: ()-> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    when(uiState){

        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                DetailsScreenShimmer()
            }
        }

        is UiState.Error -> {
            val error = uiState.message

            when(error) {
                is AppError.Network -> {
                    LaunchedEffect(error) {
                        snackbarHostState.showSnackbar(error.message)
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

            val details = uiState.data.details
//            •

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                item {

                    Box {
                        AsyncImage(
                            model = details.posterUrl,
                            contentDescription = details.title,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.6f),
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.9f)
                                        )
                                    )
                                )
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = details.title
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = details.description ?: "Description not available"
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = details.releaseDate ?: "NA"
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
