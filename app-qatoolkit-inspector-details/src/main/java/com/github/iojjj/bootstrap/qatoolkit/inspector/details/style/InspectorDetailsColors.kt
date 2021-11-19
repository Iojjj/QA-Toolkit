package com.github.iojjj.bootstrap.qatoolkit.inspector.details.style

import androidx.compose.ui.graphics.Color

data class InspectorDetailsColors(
    val isDarkTheme: Boolean,
    val stickyHeaderBackground: Color,
    val searchQueryHighlight: Color,
) {

    companion object {

        fun inspectorDetailsLightThemeColors(): InspectorDetailsColors {
            return InspectorDetailsColors(
                isDarkTheme = false,
                stickyHeaderBackground = Color(0xE6FAFAFA),
                searchQueryHighlight = Color.Red,
            )
        }

        fun inspectorDetailsDarkThemeColors(): InspectorDetailsColors {
            return InspectorDetailsColors(
                isDarkTheme = true,
                stickyHeaderBackground = Color(0xE6272727),
                searchQueryHighlight = Color.Red,
            )
        }
    }
}
