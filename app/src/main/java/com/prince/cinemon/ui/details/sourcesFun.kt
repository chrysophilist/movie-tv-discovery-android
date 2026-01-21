package com.prince.cinemon.ui.details

import com.prince.cinemon.domain.model.StreamingSource
import com.prince.cinemon.domain.model.StreamingType

fun pickBestSource(
    sources: List<StreamingSource>
): StreamingSource? {

    return sources.minByOrNull {
        when (it.type) {
            StreamingType.FREE -> 0
            StreamingType.SUBSCRIPTION -> 1
            StreamingType.RENT -> 2
            StreamingType.BUY -> 3
            else -> 4
        }
    }
}


fun String?.isValidUrl(): Boolean {
    return this != null &&
            this.startsWith("http", ignoreCase = true)
}