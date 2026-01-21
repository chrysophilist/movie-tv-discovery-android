package com.prince.cinemon.ui.settings

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.prince.cinemon.ui.components.MyHorizontalDivider
import com.prince.cinemon.ui.navigation.AppScaffoldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    navigateToApiKey: () -> Unit,
    setScaffoldState: (AppScaffoldState)-> Unit
) {

    LaunchedEffect(Unit) {
        setScaffoldState(
            AppScaffoldState(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
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

    SettingsContent(
        navigateToApiKey = navigateToApiKey
    )
}

@Composable
fun SettingsContent(
    navigateToApiKey: () -> Unit
){

    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.clip(RoundedCornerShape(22.dp))) {
            SettingsItem(icon = Icons.Outlined.VpnKey, title = "Watchmode Access", subtitle = "Manage your Watchmode API key", onClick = navigateToApiKey)
            MyHorizontalDivider()
            SettingsItem(Icons.Outlined.Feedback, "Feedback", onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto: princekumarjnvmdb@gmail.com".toUri()
                }
                try {
                    context.startActivity(intent)
                } catch(e: Exception) {
                    Toast.makeText(context, "mailto: princekumarjnvmdb@gmail.com", Toast.LENGTH_SHORT).show()
                }
            })
            MyHorizontalDivider()
            SettingsItem(Icons.Outlined.Code, "Source Code", onClick = {uriHandler.openUri("https://github.com/chrysophilist/movie-tv-discovery-android")})
            MyHorizontalDivider()
            SettingsItem(Icons.Outlined.Info, "About", onClick = {uriHandler.openUri("https://github.com/chrysophilist/movie-tv-discovery-android?tab=readme-ov-file#-movie--tv-show-discovery-app")})
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader("Developer")
        Column(modifier = Modifier.clip(RoundedCornerShape(22.dp))) {
            SettingsItem(Icons.Outlined.PersonOutline, "About Developer", onClick = {uriHandler.openUri("https://www.linkedin.com/in/princekr2480/")})
        }

        Spacer(modifier = Modifier.height(24.dp))


        SectionHeader("Other Apps")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OtherAppItem(icon = Icons.Rounded.Newspaper, label = "NewsApp", onClick = {uriHandler.openUri("https://github.com//chrysophilist/newsapp")})
            OtherAppItem(icon = Icons.Outlined.NoteAlt, label = "Noteful", onClick = {uriHandler.openUri("https://github.com/chrysophilist/Noteful")})
            OtherAppItem(icon = Icons.Outlined.Store, label = "Ecomm", onClick = {uriHandler.openUri("https://github.com/chrysophilist/Ecomm")})
            OtherAppItem(icon = Icons.Outlined.Upcoming, label = "Upcoming", onClick = {
                uriHandler.openUri("https://github.com//chrysophilist")
                Toast.makeText(context, "Thanks for your interest!", Toast.LENGTH_SHORT).show()
            })
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SectionHeader(text: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}



@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, enabled = true)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontSize = 16.sp)
            if (subtitle != null) {
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
        }
    }
}
@Composable
fun OtherAppItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp) // Touch target padding
    ) {
        // The Circular Icon Container
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // The Label Below
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
    }
}
