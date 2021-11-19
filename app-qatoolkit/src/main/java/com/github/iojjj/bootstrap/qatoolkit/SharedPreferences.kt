package com.github.iojjj.bootstrap.qatoolkit

import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

inline fun SharedPreferences.getColor(
    key: String,
    defaultValue: Color
): Color {
    return Color(getInt(key, defaultValue.toArgb()))
}

inline fun SharedPreferences.Editor.putColor(
    key: String,
    value: Color
) {
    putInt(key, value.toArgb())
}

inline fun <reified T : Enum<T>> SharedPreferences.getEnum(
    key: String,
    defaultValue: T
): T {
    return getString(key, null)
        ?.let<String, T> { enumValueOf(it) }
        ?: defaultValue
}

inline fun <T : Enum<T>> SharedPreferences.Editor.putEnum(
    key: String,
    value: T
) {
    putString(key, value.name)
}