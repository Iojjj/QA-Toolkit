package com.github.iojjj.bootstrap.qatoolkit.colorpicker.style

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorPickerColors(
    val isDarkTheme: Boolean,
    val colorIndicator: Color,
    val colorIndicatorStroke: Color,
    val hueIndicator: Color,
    val alphaIndicator: Color,
) {

    companion object {

        @Stable
        fun colorPickerLightThemeColors(): ColorPickerColors {
            return ColorPickerColors(
                isDarkTheme = false,
                colorIndicator = Color.White,
                colorIndicatorStroke = Color.DarkGray,
                hueIndicator = Color.Black,
                alphaIndicator = Color.Black,
            )
        }

        @Stable
        fun colorPickerDarkThemeColors(): ColorPickerColors {
            return ColorPickerColors(
                isDarkTheme = true,
                colorIndicator = Color.White,
                colorIndicatorStroke = Color.DarkGray,
                hueIndicator = Color.Black,
                alphaIndicator = Color.Black,
            )
        }
    }
}