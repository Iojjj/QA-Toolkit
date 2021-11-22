package com.github.iojjj.bootstrap.qatoolkit.inspector.details.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object InspectorDetailsStyle {

    val Colors: InspectorDetailsColors
        @Composable
        @ReadOnlyComposable
        get() = LocalInspectorDetailsColors.current
}