package com.prince.cinemon.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MoreVertMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (dismissMenu: () -> Unit) -> Unit
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.padding(horizontal = 8.dp).widthIn(min = 200.dp, max = 280.dp),
        shape = RoundedCornerShape(22.dp)
    ) {
        content(onDismiss)
    }
}

@Composable
fun MoreVertMenuItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    isDestructive: Boolean = false
) {
    val color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface

    DropdownMenuItem(
        text = {
            Text(
                text = text,
                fontWeight = if(isDestructive) FontWeight.Medium else FontWeight.Normal
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        onClick = {
            onClick()
            onDismiss()
        },
        colors = MenuDefaults.itemColors(
            textColor = color,
            leadingIconColor = color
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun MoreVertHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth()
    )
    MyHorizontalDivider()
}
