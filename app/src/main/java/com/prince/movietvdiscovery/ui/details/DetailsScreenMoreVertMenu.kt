package com.prince.movietvdiscovery.ui.details

import android.content.Intent
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Report
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.core.net.toUri
import com.prince.movietvdiscovery.ui.components.MoreVertHeader
import com.prince.movietvdiscovery.ui.components.MoreVertMenu
import com.prince.movietvdiscovery.ui.components.MoreVertMenuItem
import com.prince.movietvdiscovery.ui.components.MyHorizontalDivider

@Composable
fun DetailsScreenMoreVertMenu(expanded:Boolean, onDismiss: ()-> Unit) {

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    MoreVertMenu(expanded, onDismiss) { dismiss ->


        MoreVertHeader(text = "Details Screen")

        MoreVertMenuItem(
            text = "Feedback",
            icon = Icons.Default.Feedback,
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:princekumarjnvmdb@gmail.com?subject=Feedback(detailsScreen): ".toUri()
                }
                try {
                    context.startActivity(intent)
                } catch(e: Exception) {
                    Toast.makeText(context, "mailto: princekumarjnvmdb@gmail.com", Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = dismiss
        )

        MoreVertMenuItem(
            text = "Source Code",
            icon = Icons.Default.Code,
            onClick = { uriHandler.openUri("https://github.com/chrysophilist/movie-tv-discovery-android") },
            onDismiss = dismiss
        )

        MoreVertMenuItem(
            text = "App info",
            icon = Icons.Default.Info,
            onClick = { uriHandler.openUri("https://github.com/chrysophilist/movie-tv-discovery-android?tab=readme-ov-file#-movie--tv-show-discovery-app") },
            onDismiss = dismiss
        )

        MyHorizontalDivider()

        MoreVertMenuItem(
            text = "Report",
            icon = Icons.Default.Report,
            onClick = { uriHandler.openUri("https://github.com/chrysophilist/movie-tv-discovery-android/issues") },
            onDismiss = dismiss
        )
    }
}