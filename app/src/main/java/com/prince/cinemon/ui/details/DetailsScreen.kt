package com.prince.cinemon.ui.details

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.prince.cinemon.domain.model.TitleDetailsWithSources
import com.prince.cinemon.domain.util.AppError
import com.prince.cinemon.ui.common.RetrySection
import com.prince.cinemon.ui.common.UiState
import com.prince.cinemon.ui.navigation.AppScaffoldState
import org.koin.androidx.compose.koinViewModel
import androidx.core.net.toUri
import com.prince.cinemon.domain.model.StreamingType
import com.prince.cinemon.ui.common.formatRuntime
import com.prince.cinemon.ui.credits.CastSection


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

    val containerColor = if (isSystemInDarkTheme()) {Color.Black} else { MaterialTheme.colorScheme.background }

    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(titleId) {
        viewModel.loadDetails(titleId)
    }

    LaunchedEffect(uiState) {
        setScaffoldState(
            AppScaffoldState(
                containerColor = containerColor,
                applyContentPadding = false,
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.32f),
                                        shape = CircleShape
                                    )
                            ) {
                                IconButton(onClick = onBack) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back to Home",
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        actions = {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.32f),
                                        shape = CircleShape
                                    )
                            ) {
                                IconButton(onClick = { showMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More options",
                                        tint = Color.White
                                    )
                                }
                                DetailsScreenMoreVertMenu(expanded = showMenu, onDismiss = {showMenu = false})
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
                                .fillMaxWidth(0.9f)
                                .height(56.dp),
                            shape = CircleShape,
                            containerColor = Color(0xFFd91f25)

                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {

                item {

                    Box {
                        AsyncImage(
                            model = details.posterUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = if (isSystemInDarkTheme()){
                                            listOf(
                                            Color.Black.copy(alpha = 0.6f),
                                            Color.Black.copy(alpha = 0.3f),
                                            Color.Transparent,
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.3f),
                                            Color.Black.copy(alpha = 0.6f),
                                            Color.Black.copy(alpha = 1f)
                                        )
                                        } else {
                                            listOf(
                                                MaterialTheme.colorScheme.background.copy(0.2f),
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.background.copy(0.5f),
                                                MaterialTheme.colorScheme.background.copy(0.7f),
                                                MaterialTheme.colorScheme.background.copy(0.9f),
                                                MaterialTheme.colorScheme.background.copy(1f),
                                            )
                                        }
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = details.title,
                                fontSize = 28.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Badge(text = details.year.toString())
                                Spacer(modifier = Modifier.width(8.dp))
                                Badge(text = details.genres.firstOrNull() ?: "Movie")
                                Spacer(modifier = Modifier.width(8.dp))
                                Badge(text = formatRuntime(details.runtimeMinutes))
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Storyline",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = details.description ?: "No description available.",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            lineHeight = 24.sp
                        )
                        Spacer(Modifier.height(24.dp))

                        if (details.cast.isNotEmpty()) {
                            CastSection(castList = details.cast, onSeeAllClick = {})
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Badge(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}