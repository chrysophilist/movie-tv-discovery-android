package com.prince.cinemon.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prince.cinemon.ui.common.shimmer


@Composable
fun DetailsScreenShimmer() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {

            // Poster shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .shimmer()
            )

            Spacer(Modifier.height(8.dp))

            // Title shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(24.dp)
                    .shimmer()
            )

            Spacer(Modifier.height(8.dp))

            // Description shimmer (3 lines)
            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .shimmer()
                )
                Spacer(Modifier.height(6.dp))
            }

            Spacer(Modifier.height(8.dp))

            // Release date shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(14.dp)
                    .shimmer()
            )
        }
    }
}
