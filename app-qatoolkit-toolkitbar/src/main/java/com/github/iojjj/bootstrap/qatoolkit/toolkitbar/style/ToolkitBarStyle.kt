package com.github.iojjj.bootstrap.qatoolkit.toolkitbar.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ToolkitBarStyle {

    val Colors: ToolkitBarColors
        @Composable
        @ReadOnlyComposable
        get() = LocalToolkitBarColors.current

    val CardElevation: Dp = 4.dp

    const val DisabledAlpha: Float = 0.24f

    const val DividerAlpha: Float = 0.12f
}
