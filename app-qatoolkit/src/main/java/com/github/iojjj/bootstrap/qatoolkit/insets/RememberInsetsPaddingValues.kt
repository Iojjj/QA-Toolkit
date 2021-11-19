package com.github.iojjj.bootstrap.qatoolkit.insets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.Insets
import com.google.accompanist.insets.rememberInsetsPaddingValues

@Composable
inline fun rememberInsetsPaddingValues(
    insets: Insets,
    applyStart: Boolean = true,
    applyTop: Boolean = true,
    applyEnd: Boolean = true,
    applyBottom: Boolean = true,
    additional: Dp,
): PaddingValues {
    return rememberInsetsPaddingValues(
        insets = insets,
        applyStart = applyStart,
        applyTop = applyTop,
        applyEnd = applyEnd,
        applyBottom = applyBottom,
        additionalStart = additional,
        additionalTop = additional,
        additionalEnd = additional,
        additionalBottom = additional,
    )
}

@Composable
inline fun rememberInsetsPaddingValues(
    insets: Insets,
    applyStart: Boolean = true,
    applyTop: Boolean = true,
    applyEnd: Boolean = true,
    applyBottom: Boolean = true,
    additionalHorizontal: Dp = 0.dp,
    additionalVertical: Dp = 0.dp,
): PaddingValues {
    return rememberInsetsPaddingValues(
        insets = insets,
        applyStart = applyStart,
        applyTop = applyTop,
        applyEnd = applyEnd,
        applyBottom = applyBottom,
        additionalStart = additionalHorizontal,
        additionalTop = additionalVertical,
        additionalEnd = additionalHorizontal,
        additionalBottom = additionalVertical,
    )
}