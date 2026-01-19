package com.prince.movietvdiscovery.ui.apikey

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prince.movietvdiscovery.domain.model.ApiKeyStatus

@Composable
fun ApiKeyStatusCard(state: ApiKeyUiState) {
    val isDark = isSystemInDarkTheme()
    val statusRed = if (isDark) StatusRedDark else StatusRedLight

    val uiData = when (val status = state.status) {
        is ApiKeyStatus.Valid -> {
            val total = status.quota.takeIf { it > 0 } ?: 1
            val progress = status.quotaRemaining.toFloat() / total.toFloat()
            StatusUiData(
                indicatorColor = StatusGreen,
                progress = progress,
                mainText = status.quotaRemaining.toString(),
                subText = "Requests Left",
                row1Label = "Total Quota",
                row1Value = status.quota.toString(),
                row2Label = "Used",
                row2Value = status.quotaUsed.toString()
            )
        }
        ApiKeyStatus.Missing -> StatusUiData(
            indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
            progress = 0f,
            mainText = "--",
            subText = "No Key",
            row1Label = "Status",
            row1Value = "Missing",
            row2Label = "Action",
            row2Value = "Enter Key"
        )
        ApiKeyStatus.Invalid -> StatusUiData(
            indicatorColor = statusRed,
            progress = 1f,
            mainText = "!",
            subText = "Invalid Key",
            row1Label = "Status",
            row1Value = "Rejected",
            row2Label = "Reason",
            row2Value = "Key not recognized"
        )
        is ApiKeyStatus.Error -> StatusUiData(
            indicatorColor = statusRed,
            progress = 0f,
            mainText = "ERR",
            subText = "Error",
            row1Label = "Status",
            row1Value = "Failed",
            row2Label = "Message",
            row2Value = status.message
        )
        is ApiKeyStatus.QuotaExceeded -> {
            val total = status.quota.takeIf { it > 0 } ?: 1
            StatusUiData(
                indicatorColor = statusRed,
                progress = 1f,
                mainText = "0",
                subText = "Requests Left",
                row1Label = "Quota Used",
                row1Value = "${status.quotaUsed} / ${status.quota}",
                row2Label = "Resets In",
                row2Value = "${status.resetInDays} days"
            )
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Header
            Text(
                text = "API STATUS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Circular Indicator
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Background Track
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(220.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    strokeWidth = 12.dp,
                    trackColor = Color.Transparent,
                )

                // Animated Progress
                val animatedProgress by animateFloatAsState(
                    targetValue = uiData.progress,
                    animationSpec = tween(durationMillis = 800),
                    label = "progress"
                )

                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(220.dp),
                    color = uiData.indicatorColor,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round,
                    trackColor = Color.Transparent
                )

                // Center Text
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiData.mainText,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = uiData.subText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Bottom Stats
            StatRow(label = uiData.row1Label, value = uiData.row1Value)
            Spacer(modifier = Modifier.height(16.dp))
            StatRow(label = uiData.row2Label, value = uiData.row2Value)
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

data class StatusUiData(
    val indicatorColor: Color,
    val progress: Float,
    val mainText: String,
    val subText: String,
    val row1Label: String,
    val row1Value: String,
    val row2Label: String,
    val row2Value: String
)