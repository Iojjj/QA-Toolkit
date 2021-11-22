package com.github.iojjj.bootstrap.qatoolkit.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import java.text.NumberFormat

val LocalPxFormatter = compositionLocalOf<(Float) -> String> {
    error("PX formatter is not provided.")
}

@Composable
fun rememberPxFormatter(): (Float) -> String {
    val configuration = LocalConfiguration.current
    return remember(configuration) {
        val numberFormatter = NumberFormat.getIntegerInstance();
        { size ->
            "${numberFormatter.format(size)}px"
        }
    }
}