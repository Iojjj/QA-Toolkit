package com.github.iojjj.bootstrap.qatoolkit.grid

import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.ColorPicker
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.rememberColorPickerState
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalDpFormatter
import com.github.iojjj.bootstrap.qatoolkit.grid.GridSettingsMenuItem.CellSize
import com.github.iojjj.bootstrap.qatoolkit.grid.GridSettingsMenuItem.GridSize
import com.github.iojjj.bootstrap.qatoolkit.grid.GridSettingsMenuItem.StrokeColor
import com.github.iojjj.bootstrap.qatoolkit.grid.GridSettingsMenuItem.StrokeScale
import com.github.iojjj.bootstrap.qatoolkit.settings.NumberSlider
import com.github.iojjj.bootstrap.qatoolkit.settings.ScaleSlider
import com.github.iojjj.bootstrap.qatoolkit.settings.SettingsPanel

@ExperimentalMaterialApi
@Composable
fun GridSettingsPanel(
    strokeScale: Float,
    onStrokeScaleChange: (Float) -> Unit,
    strokeColor: Color,
    onStrokeColorChange: (Color) -> Unit,
    gridSize: Int,
    onGridSizeChange: (Int) -> Unit,
    cellSize: Int,
    onCellSizeChange: (Int) -> Unit,
    onBottomSheetStateChange: (BottomSheetValue) -> Unit,
) {
    val menuItems = remember {
        listOf(
            StrokeScale,
            StrokeColor,
            GridSize,
            CellSize,
        )
    }
    val strokeScaleState = rememberUpdatedState(strokeScale)
    val onStrokeScaleChangeState = rememberUpdatedState(onStrokeScaleChange)
    val strokeColorState = rememberUpdatedState(strokeColor)
    val onStrokeColorChangeState = rememberUpdatedState(onStrokeColorChange)
    val gridSizeState = rememberUpdatedState(gridSize)
    val onGridSizeChangeState = rememberUpdatedState(onGridSizeChange)
    val cellSizeState = rememberUpdatedState(cellSize)
    val onCellSizeChangeState = rememberUpdatedState(onCellSizeChange)
    val menuItemContentProvider = remember<@Composable (GridSettingsMenuItem) -> Unit>(
        strokeScale, onStrokeScaleChange, strokeColor, onStrokeColorChange, gridSize, onGridSizeChange
    ) {
        { menuItem ->
            when (menuItem) {
                is StrokeScale -> {
                    ScaleSlider(
                        scale = strokeScaleState.value,
                        onScaleChange = onStrokeScaleChangeState.value
                    )
                }
                is StrokeColor -> {
                    ColorPicker(rememberColorPickerState(strokeColorState.value, onStrokeColorChangeState.value))
                }
                is GridSize -> {
                    NumberSlider(
                        valueFrom = 2,
                        valueTo = 10,
                        value = gridSizeState.value,
                        onValueChange = onGridSizeChangeState.value,
                    )
                }
                is CellSize -> {
                    val dpFormatter = LocalDpFormatter.current
                    NumberSlider(
                        valueFrom = 8,
                        valueTo = 32,
                        value = cellSizeState.value,
                        onValueChange = onCellSizeChangeState.value,
                        labelFormatter = dpFormatter
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
fun PreviewGridSettingsPanel() {
    val (strokeScale, onStrokeScaleChange) = remember { mutableStateOf(1.0f) }
    val (strokeColor, onStrokeColorChange) = remember { mutableStateOf(Color.Red.copy(alpha = 0.5f)) }
    val (gridSize, onGridSizeChange) = remember { mutableStateOf(5) }
    val (cellSize, onCellSizeChange) = remember { mutableStateOf(8) }
    GridSettingsPanel(
        strokeScale = strokeScale,
        onStrokeScaleChange = onStrokeScaleChange,
        strokeColor = strokeColor,
        onStrokeColorChange = onStrokeColorChange,
        gridSize = gridSize,
        onGridSizeChange = onGridSizeChange,
        cellSize = cellSize,
        onCellSizeChange = onCellSizeChange,
        onBottomSheetStateChange = {}
    )
}

