package com.prince.cinemon.ui.apikey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prince.cinemon.domain.model.ApiKeyStatus
import com.prince.cinemon.ui.navigation.AppScaffoldState
import com.prince.cinemon.ui.theme.MovieTVDiscoveryTheme
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyRoute(
    viewModel: ApiKeyViewModel = koinViewModel(),
    setScaffoldState: (AppScaffoldState) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showMenu by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        setScaffoldState(
            AppScaffoldState(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Watchmode API", fontWeight = FontWeight.SemiBold) },
                        navigationIcon = {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                            }
                        },
                        actions = {
                            IconButton( onClick = { showMenu = true },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "Menu"
                                )
                            }
                            ApiKeyScreenMoreVertMenu(expanded = showMenu, onDismiss = { showMenu = false })
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent
                        )
                    )
                }
            )
        )
    }

    ApiKeyScreen(
        state = uiState,
        onSave = viewModel::saveApiKey,
        onTest = viewModel::validateKey,
        onClear = viewModel::clearApiKey
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyScreen(
    state: ApiKeyUiState,
    onSave: (String) -> Unit,
    onTest: () -> Unit,
    onClear: () -> Unit
) {
    var inputKey by remember(state.currentKey) { mutableStateOf(state.currentKey.orEmpty()) }
    var showGuideSheet by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val focusManager = LocalFocusManager.current
    var showKey by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
//            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ApiKeyStatusCard(state)
        }
        item {
            OutlinedTextField(
                value = inputKey,
                onValueChange = { inputKey = it },
                label = { Text("API Key") },
                placeholder = { Text("Enter your Watchmode key") },
                leadingIcon = {
                    Icon(Icons.Default.Key, null)
                },
                trailingIcon = {
                    IconButton(onClick = { showKey = !showKey }) {
                        Icon(
                            imageVector = if (showKey) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (showKey)
                                "Hide API key"
                            else
                                "Show API key"
                        )
                    }
                },
                visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (showKey) KeyboardType.Text else KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }
        if (
            state.status is ApiKeyStatus.Missing ||
            state.status is ApiKeyStatus.Invalid ||
            state.status is ApiKeyStatus.Error ||
            state.status is ApiKeyStatus.QuotaExceeded
            ) {
            item {
                GetApiKeyCard(
                    onClick = { showGuideSheet = true }
                )
                Spacer(Modifier.height(24.dp))
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        onSave(inputKey)
                        focusManager.clearFocus()
                        onTest()
                    },
                    enabled = inputKey.isNotBlank(),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Save")
                }

                OutlinedButton(
                    onClick = {
                        onTest()
                        focusManager.clearFocus()
                    },
                    enabled = inputKey.isNotBlank() && !state.isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text("Test")
                    }
                }
            }
        }
        item {
            TextButton(
                onClick = { showClearDialog = true },
                enabled = !state.currentKey.isNullOrBlank()
            ) {
                Text(
                    "Clear API Key",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    if (showClearDialog) {
        ClearKeyDialog(
            onConfirm = {
                onClear()
                showClearDialog = false
            },
            onDismiss = { showClearDialog = false }
        )
    }

    if (showGuideSheet) {
        HowToGetKeySheet(
            onDismiss = { showGuideSheet = false },
            onGoToWebsite = {
                showGuideSheet = false
                uriHandler.openUri("https://api.watchmode.com/requestApiKey/")
            }
        )
    }
}


@Composable
fun ClearKeyDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Delete API Key ?")
        },
        text = {
            Text(
                text = "This will permanently delete the saved API key from this device.\n\nYou can always retrieve your key from your Watchmode account.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        icon = {
            Icon(Icons.Default.DeleteForever, contentDescription = null)
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewValidState() {
    MovieTVDiscoveryTheme {
        ApiKeyScreen(
            state = ApiKeyUiState(
                status = ApiKeyStatus.Valid(1000, 250, 750),
                currentKey = "123"
            ),
            {}, {}, {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewErrorState() {
    MovieTVDiscoveryTheme {
        ApiKeyScreen(
            state = ApiKeyUiState(
                status = ApiKeyStatus.Error("Network Timeout"),
                currentKey = "123"
            ),
            {}, {}, {}
        )
    }
}

@Preview(name = "State: Missing (Default)", showBackground = true)
@Composable
fun PreviewMissingState() {
    MovieTVDiscoveryTheme {
        ApiKeyScreen(
            state = ApiKeyUiState(
                status = ApiKeyStatus.Missing,
                currentKey = null,
                isLoading = false
            ),
            onSave = {},
            onTest = {},
            onClear = {}
        )
    }
}

@Preview(name = "State: Invalid Key", showBackground = true)
@Composable
fun PreviewInvalidState() {
    MovieTVDiscoveryTheme {
        ApiKeyScreen(
            state = ApiKeyUiState(
                status = ApiKeyStatus.Invalid,
                currentKey = "invalid_key_123",
                isLoading = false
            ),
            onSave = {},
            onTest = {},
            onClear = {}
        )
    }
}