package com.github.iojjj.bootstrap.qatoolkit.toolkitbar.style

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Immutable
data class ToolkitBarColors(
    val isDarkTheme: Boolean,
    val background: Color,
    val onBackground: Color,
    val onBackgroundChecked: Color,
) {

    @Stable
    fun contentColor(
        isChecked: Boolean,
        isEnabled: Boolean
    ): Color {
        val color = when (isChecked) {
            true -> onBackgroundChecked
            false -> onBackground
        }
        return when (isEnabled) {
            true -> color
            false -> color.copy(alpha = ToolkitBarStyle.DisabledAlpha)
        }
    }


    companion object {

        @Stable
        fun toolkitBarLightThemeColors(): ToolkitBarColors {
            return ToolkitBarColors(
                isDarkTheme = false,
                background = Color.White,
                onBackground = Color.Black.copy(alpha = 0.56f),
                onBackgroundChecked = Color.Black,
            )
        }

        @Stable
        fun toolkitBarDarkThemeColors(): ToolkitBarColors {
            return ToolkitBarColors(
                isDarkTheme = true,
                background = Color(0xFF272727),
                onBackground = Color.White.copy(alpha = 0.87f),
                onBackgroundChecked = Color.White,
            )
        }
    }
}