package com.github.iojjj.bootstrap.qatoolkit.inspector

import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.ColorPicker
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.rememberColorPickerState
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorSettingsMenuItem.ArrowColor
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorSettingsMenuItem.NodeStrokeColor
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorSettingsMenuItem.NodeStrokeScale
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorSettingsMenuItem.OverlayColor
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorSettingsMenuItem.PinnedStrokeColor
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorSettingsMenuItem.SelectedStrokeColor
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorSettingsMenuItem.TextColor
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorSettingsMenuItem.TextScale
import com.github.iojjj.bootstrap.qatoolkit.settings.ScaleSlider
import com.github.iojjj.bootstrap.qatoolkit.settings.SettingsPanel

@ExperimentalMaterialApi
@Composable
fun InspectorSettingsPanel(
    overlayColor: Color,
    onOverlayColorChange: (newOverlayColor: Color) -> Unit,
    arrowColor: Color,
    onArrowColorChange: (newArrowColor: Color) -> Unit,
    nodeStrokeScale: Float,
    onNodeStrokeScaleChange: (newNodeStrokeScale: Float) -> Unit,
    nodeStrokeColor: Color,
    onNodeStrokeColorChange: (newNodeStrokeColor: Color) -> Unit,
    selectedStrokeColor: Color,
    onSelectedStrokeColorChange: (newSelectedStrokeColor: Color) -> Unit,
    pinnedStrokeColor: Color,
    onPinnedStrokeColorChange: (newPinnedStrokeColor: Color) -> Unit,
    textScale: Float,
    onTextScaleChange: (newTextScale: Float) -> Unit,
    textColor: Color,
    onTextColorChange: (newTextColor: Color) -> Unit,
    onBottomSheetStateChange: (BottomSheetValue) -> Unit
) {
    val menuItems = remember {
        listOf(
            OverlayColor,
            ArrowColor,
            NodeStrokeScale,
            NodeStrokeColor,
            SelectedStrokeColor,
            PinnedStrokeColor,
            TextScale,
            TextColor,
        )
    }
    val overlayColorState = rememberUpdatedState(overlayColor)
    val onOverlayColorChangeState = rememberUpdatedState(onOverlayColorChange)
    val arrowColorState = rememberUpdatedState(arrowColor)
    val onArrowColorChangeState = rememberUpdatedState(onArrowColorChange)
    val nodeStrokeScaleState = rememberUpdatedState(nodeStrokeScale)
    val onNodeStrokeScaleChangeState = rememberUpdatedState(onNodeStrokeScaleChange)
    val nodeStrokeColorState = rememberUpdatedState(nodeStrokeColor)
    val onNodeStrokeColorChangeState = rememberUpdatedState(onNodeStrokeColorChange)
    val selectedStrokeColorState = rememberUpdatedState(selectedStrokeColor)
    val onSelectedStrokeColorChangeState = rememberUpdatedState(onSelectedStrokeColorChange)
    val pinnedStrokeColorState = rememberUpdatedState(pinnedStrokeColor)
    val onPinnedStrokeColorChangeState = rememberUpdatedState(onPinnedStrokeColorChange)
    val textScaleState = rememberUpdatedState(textScale)
    val onTextScaleChangeState = rememberUpdatedState(onTextScaleChange)
    val textColorState = rememberUpdatedState(textColor)
    val onTextColorChangeState = rememberUpdatedState(onTextColorChange)

    val menuItemContentProvider = remember<@Composable (InspectorSettingsMenuItem) -> Unit> {
        { menuItem ->
            exhaustive..when (menuItem) {
                OverlayColor -> {
                    ColorPicker(rememberColorPickerState(overlayColorState.value, onOverlayColorChangeState.value))
                }
                ArrowColor -> {
                    ColorPicker(rememberColorPickerState(arrowColorState.value, onArrowColorChangeState.value))
                }
                NodeStrokeScale -> {
                    ScaleSlider(
                        minScale = 0.5f,
                        maxScale = 3.0f,
                        scale = nodeStrokeScaleState.value,
                        onScaleChange = onNodeStrokeScaleChangeState.value
                    )
                }
                NodeStrokeColor -> {
                    ColorPicker(rememberColorPickerState(nodeStrokeColorState.value, onNodeStrokeColorChangeState.value))
                }
                PinnedStrokeColor -> {
                    ColorPicker(rememberColorPickerState(pinnedStrokeColorState.value, onPinnedStrokeColorChangeState.value))
                }
                SelectedStrokeColor -> {
                    ColorPicker(rememberColorPickerState(selectedStrokeColorState.value, onSelectedStrokeColorChangeState.value))
                }
                TextScale -> {
                    ScaleSlider(
                        minScale = 0.5f,
                        maxScale = 2.0f,
                        scale = textScaleState.value,
                        onScaleChange = onTextScaleChangeState.value
                    )
                }
                TextColor -> {
                    ColorPicker(rememberColorPickerState(textColorState.value, onTextColorChangeState.value))
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
