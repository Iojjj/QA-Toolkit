package com.github.iojjj.bootstrap.qatoolkit.settings.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SettingsStyle {

    val Colors: SettingsColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSettingsColors.current

    val CardElevation: Dp = 4.dp

    const val DisabledAlpha: Float = 0.24f
}
