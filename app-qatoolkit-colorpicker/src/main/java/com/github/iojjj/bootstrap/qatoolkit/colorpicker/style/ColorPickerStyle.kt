package com.github.iojjj.bootstrap.qatoolkit.colorpicker.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object ColorPickerStyle {

    val Colors: ColorPickerColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColorPickerColors.current

}
