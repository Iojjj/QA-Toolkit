package com.github.iojjj.bootstrap.qatoolkit.ruler

import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.ColorPicker
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.rememberColorPickerState
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerSettingsMenuItem.Sensitivity
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerSettingsMenuItem.StrokeColor
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerSettingsMenuItem.StrokeScale
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerSettingsMenuItem.TextColor
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerSettingsMenuItem.TextScale
import com.github.iojjj.bootstrap.qatoolkit.settings.ScaleSlider
import com.github.iojjj.bootstrap.qatoolkit.settings.SettingsPanel
import com.github.iojjj.bootstrap.qatoolkit.settings.SliderMode

@ExperimentalMaterialApi
@Composable
fun RulerSettingsPanel(
    strokeScale: Float,
    onStrokeScaleChange: (Float) -> Unit,
    strokeColor: Color,
    onStrokeColorChange: (Color) -> Unit,
    textScale: Float,
    onTextScaleChange: (Float) -> Unit,
    textColor: Color,
    onTextColorChange: (Color) -> Unit,
    sensitivity: Float,
    onSensitivityChange: (Float) -> Unit,
    onBottomSheetStateChange: (BottomSheetValue) -> Unit,
) {
    val menuItems = remember {
        listOf(
            StrokeScale,
            StrokeColor,
            TextScale,
            TextColor,
            Sensitivity,
        )
    }
    val strokeScaleState = rememberUpdatedState(strokeScale)
    val onStrokeScaleChangeState = rememberUpdatedState(onStrokeScaleChange)
    val strokeColorState = rememberUpdatedState(strokeColor)
    val onStrokeColorChangeState = rememberUpdatedState(onStrokeColorChange)
    val textScaleState = rememberUpdatedState(textScale)
    val onTextScaleChangeState = rememberUpdatedState(onTextScaleChange)
    val textColorState = rememberUpdatedState(textColor)
    val onTextColorChangeState = rememberUpdatedState(onTextColorChange)
    val sensitivityState = rememberUpdatedState(sensitivity)
    val onSensitivityChangeState = rememberUpdatedState(onSensitivityChange)

    val menuItemContentProvider = remember<@Composable (RulerSettingsMenuItem) -> Unit> {
        { menuItem ->
            exhaustive..when (menuItem) {
                is StrokeScale -> {
                    ScaleSlider(
                        minScale = 0.5f,
                        maxScale = 3.0f,
                        scale = strokeScaleState.value,
                        onScaleChange = onStrokeScaleChangeState.value
                    )
                }
                is StrokeColor -> {
                    ColorPicker(rememberColorPickerState(strokeColorState.value, onStrokeColorChangeState.value))
                }
                is TextScale -> {
                    ScaleSlider(
                        minScale = 0.5f,
                        maxScale = 2.0f,
                        scale = textScaleState.value,
                        onScaleChange = onTextScaleChangeState.value,
                        mode = SliderMode.Continuous,
                    )
                }
                is TextColor -> {
                    ColorPicker(
                        state = rememberColorPickerState(textColorState.value, onTextColorChangeState.value),
                    )
                }
                is Sensitivity -> {
                    ScaleSlider(
                        minScale = 0.1f,
                        maxScale = 1.0f,
                        mode = SliderMode.Continuous,
                        scale = sensitivityState.value,
                        onScaleChange = onSensitivityChangeState.value
                    )
                }
            }
        }
    }
    SettingsPanel(
        menuItems = menuItems,
        menuItemContent = menuItemContentProvider,
        onBottomSheetStateChange = onBottomSheetStateChange
    )
}

@Preview
@ExperimentalMaterialApi
@Composable
fun PreviewRulerSettingsPanel() {
    val (strokeScale, onStrokeScaleChange) = remember { mutableStateOf(1.0f) }
    val (strokeColor, onStrokeColorChange) = remember { mutableStateOf(Color.Red.copy(alpha = 0.5f)) }
    val (textScale, onTextScaleChange) = remember { mutableStateOf(1.0f) }
    val (textColor, onTextColorChange) = remember { mutableStateOf(Color.White) }
    val (sensitivity, onSensitivityChange) = remember { mutableStateOf(1.0f) }
    RulerSettingsPanel(
        strokeScale = strokeScale,
        onStrokeScaleChange = onStrokeScaleChange,
        strokeColor = strokeColor,
        onStrokeColorChange = onStrokeColorChange,
        textScale = textScale,
        onTextScaleChange = onTextScaleChange,
        textColor = textColor,
        onTextColorChange = onTextColorChange,
        sensitivity = sensitivity,
        onSensitivityChange = onSensitivityChange,
        onBottomSheetStateChange = {}
    )
}
