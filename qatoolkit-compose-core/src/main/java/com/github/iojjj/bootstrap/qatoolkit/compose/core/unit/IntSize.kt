package com.github.iojjj.bootstrap.qatoolkit.compose.core.unit

import androidx.compose.ui.unit.IntSize

val IntSize.minDimension: Int
    get() = minOf(width, height)
val IntSize.maxDimension: Int
    get() = maxOf(width, height)