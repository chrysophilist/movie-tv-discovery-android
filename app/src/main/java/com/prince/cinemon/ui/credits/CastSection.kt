package com.prince.cinemon.ui.credits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prince.cinemon.domain.model.Credit
import com.prince.cinemon.domain.model.CreditType
import com.prince.cinemon.ui.details.isValidUrl


@Composable
fun CastSection(
    castList: List<Credit>,
    onSeeAllClick: () -> Unit
    ){

    val castList = remember(castList) {
        castList
            .filter { it.type == CreditType.CAST }
            .filter { it.imageUrl.isValidUrl() }
    }

    if (castList.isEmpty()) {
        return
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Cast & Crew",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onSeeAllClick) {
                Text(
                    text = "See all",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = castList,
                key = { it.id }
            ){ cast ->
                CreditItem(
                    credit = cast
                )
            }
        }
    }
}
