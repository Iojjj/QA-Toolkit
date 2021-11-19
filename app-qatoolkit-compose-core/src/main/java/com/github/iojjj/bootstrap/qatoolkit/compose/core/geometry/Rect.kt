package com.github.iojjj.bootstrap.qatoolkit.compose.core.geometry

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

fun Rect.containsInclusive(offset: Offset): Boolean {
    return offset.x in left..right && offset.y in top..bottom
}