package com.github.iojjj.bootstrap.qatoolkit.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

fun lightThemeColors(): Colors {
    return lightColors(
        background = Color.White,
        surface = Color.White,
        primary = Color(0xFF1976d2),
        primaryVariant = Color(0xFF004ba0),
        onPrimary = Color.White,
        secondary = Color(0xFF4caf50),
        secondaryVariant = Color(0xFF087f23),
        onSecondary = Color.Black
    )
}

fun darkThemeColors(): Colors {
    return darkColors(
        background = Color(0xFF121212),
        surface = Color(0xFF121212),
        primary = Color(0xFF90CAF9),
        primaryVariant = Color(0xFF004ba0),
        onPrimary = Color.Black,
        secondary = Color(0xFF4caf50),
        secondaryVariant = Color(0xFF087f23),
        onSecondary = Color.Black
    )
}