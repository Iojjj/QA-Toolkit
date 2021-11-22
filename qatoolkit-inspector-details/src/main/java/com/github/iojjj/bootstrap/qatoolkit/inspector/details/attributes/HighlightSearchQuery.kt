package com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

internal fun highlightSearchQuery(
    text: String,
    query: String,
    color: Color
): AnnotatedString {
    return buildAnnotatedString {
        if (query.isEmpty()) {
            append(text)
        } else {
            val queryLength = query.length
            val style = SpanStyle(color = color)
            var startIndex = 0
            while (startIndex >= 0) {
                val index = text.indexOf(query, startIndex, ignoreCase = true)
                startIndex = if (index == -1) {
                    append(text.substring(startIndex, text.length))
                    -1
                } else {
                    if (index != startIndex) {
                        append(text.substring(startIndex, index))
                    }
                    pushStyle(style)
                    val endIndex = index + queryLength
                    append(text.substring(index, endIndex))
                    pop()
                    endIndex
                }
            }
        }
    }
}