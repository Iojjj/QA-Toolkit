package com.github.iojjj.bootstrap.qatoolkit.settings.style

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Immutable
data class SettingsColors(
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
            false -> color.copy(alpha = SettingsStyle.DisabledAlpha)
        }
    }


    companion object {

        @Stable
        fun settingsLightThemeColors(): SettingsColors {
            return SettingsColors(
                isDarkTheme = false,
                background = Color.White,
                onBackground = Color.Black.copy(alpha = 0.56f),
                onBackgroundChecked = Color.Black,
            )
        }

        @Stable
        fun settingsDarkThemeColors(): SettingsColors {
            return SettingsColors(
                isDarkTheme = true,
                background = Color(0xFF272727),
                onBackground = Color.White.copy(alpha = 0.87f),
                onBackgroundChecked = Color.White,
            )
        }
    }
}