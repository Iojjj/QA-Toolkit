package com.github.iojjj.bootstrap.qatoolkit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.style.ColorPickerColors
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.style.ColorPickerColors.Companion.colorPickerDarkThemeColors
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.style.ColorPickerColors.Companion.colorPickerLightThemeColors
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.style.LocalColorPickerColors
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.style.InspectorDetailsColors
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.style.InspectorDetailsColors.Companion.inspectorDetailsDarkThemeColors
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.style.InspectorDetailsColors.Companion.inspectorDetailsLightThemeColors
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.style.LocalInspectorDetailsColors
import com.github.iojjj.bootstrap.qatoolkit.settings.style.LocalSettingsColors
import com.github.iojjj.bootstrap.qatoolkit.settings.style.SettingsColors
import com.github.iojjj.bootstrap.qatoolkit.settings.style.SettingsColors.Companion.settingsDarkThemeColors
import com.github.iojjj.bootstrap.qatoolkit.settings.style.SettingsColors.Companion.settingsLightThemeColors
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.style.LocalToolkitBarColors
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.style.ToolkitBarColors
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.style.ToolkitBarColors.Companion.toolkitBarDarkThemeColors
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.style.ToolkitBarColors.Companion.toolkitBarLightThemeColors

@Composable
fun QaToolkitTheme(
    content: @Composable () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val materialThemColors = remember(isDarkTheme) {
        when (isDarkTheme) {
            true -> darkThemeColors()
            false -> lightThemeColors()
        }
    }
    MaterialTheme(
        colors = materialThemColors
    ) {
        val secondaryColor = MaterialTheme.colors.secondary
        val toolkitBarColors = rememberToolkitBarColors(isDarkTheme, secondaryColor)
        val settingsColors = rememberSettingsColors(isDarkTheme, secondaryColor)
        val colorPickerColors = rememberColorPickerColors(isDarkTheme)
        val inspectorDetailsColors = rememberInspectorDetailsColors(isDarkTheme, secondaryColor)
        CompositionLocalProvider(
            LocalToolkitBarColors provides toolkitBarColors,
            LocalSettingsColors provides settingsColors,
            LocalColorPickerColors provides colorPickerColors,
            LocalInspectorDetailsColors provides inspectorDetailsColors,
        ) {
            content()
        }
    }
}

@Composable
private fun rememberToolkitBarColors(
    isDarkTheme: Boolean,
    secondaryColor: Color
): ToolkitBarColors {
    return remember(isDarkTheme, secondaryColor) {
        when (isDarkTheme) {
            true -> toolkitBarDarkThemeColors().copy(onBackgroundChecked = secondaryColor)
            false -> toolkitBarLightThemeColors().copy(onBackgroundChecked = secondaryColor)
        }
    }
}

@Composable
private fun rememberSettingsColors(
    isDarkTheme: Boolean,
    secondaryColor: Color
): SettingsColors {
    return remember(isDarkTheme, secondaryColor) {
        when (isDarkTheme) {
            true -> settingsDarkThemeColors().copy(onBackgroundChecked = secondaryColor)
            false -> settingsLightThemeColors().copy(onBackgroundChecked = secondaryColor)
        }
    }
}

@Composable
private fun rememberColorPickerColors(
    isDarkTheme: Boolean
): ColorPickerColors {
    return remember(isDarkTheme) {
        when (isDarkTheme) {
            true -> colorPickerDarkThemeColors()
            false -> colorPickerLightThemeColors()
        }
    }
}

@Composable
private fun rememberInspectorDetailsColors(
    isDarkTheme: Boolean,
    secondaryColor: Color
): InspectorDetailsColors {
    return remember(isDarkTheme, secondaryColor) {
        when (isDarkTheme) {
            true -> inspectorDetailsDarkThemeColors().copy(searchQueryHighlight = secondaryColor)
            false -> inspectorDetailsLightThemeColors().copy(searchQueryHighlight = secondaryColor)
        }
    }
}