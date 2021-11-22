package com.github.iojjj.bootstrap.qatoolkit.grid

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun GridModeLayout(
    strokeScale: Float,
    onStrokeScaleChange: (Float) -> Unit,
    strokeColor: Color,
    onStrokeColorChange: (Color) -> Unit,
    gridSize: Int,
    onGridSizeChange: (Int) -> Unit,
    cellSize: Int,
    onCellSizeChange: (Int) -> Unit,
    horizontalSnapPosition: SnapPosition,
    verticalSnapPosition: SnapPosition,
    isSettingsVisible: Boolean,
    onAreSettingsVisibleChange: (Boolean) -> Unit,
) {
    val onAreSettingsVisibleChangeState = rememberUpdatedState(onAreSettingsVisibleChange)
    Box {
        Grid(
            strokeScale = strokeScale,
            strokeColor = strokeColor,
            gridSize = gridSize,
            cellSize = cellSize,
            onCellSizeChange = onCellSizeChange,
            minCellSize = 8,
            maxCellSize = 32,
            cellSizeChangeScaleRatio = 0.25f,
            horizontalSnapPosition = horizontalSnapPosition,
            verticalSnapPosition = verticalSnapPosition,
        )
        AnimatedVisibility(
            visible = isSettingsVisible,
            enter = slideInVertically(
                initialOffsetY = { height -> height },
            ),
            exit = slideOutVertically(
                targetOffsetY = { height -> height },
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            val onBottomSheetStateChange = remember<(BottomSheetValue) -> Unit> {
                {
                    onAreSettingsVisibleChangeState.value.invoke(it == BottomSheetValue.Expanded)
                }
            }
            GridSettingsPanel(
                strokeScale = strokeScale,
                onStrokeScaleChange = onStrokeScaleChange,
                strokeColor = strokeColor,
                onStrokeColorChange = onStrokeColorChange,
                gridSize = gridSize,
                onGridSizeChange = onGridSizeChange,
                cellSize = cellSize,
                onCellSizeChange = onCellSizeChange,
                onBottomSheetStateChange = onBottomSheetStateChange
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewGridModeLayout() {
    val (strokeScale, onStrokeScaleChange) = remember { mutableStateOf(1.0f) }
    val (strokeColor, onStrokeColorChange) = remember { mutableStateOf(Color.Red.copy(alpha = 0.5f)) }
    val (gridSize, onGridSizeChange) = remember { mutableStateOf(5) }
    val (cellSize, onCellSizeChange) = remember { mutableStateOf(8) }
    GridModeLayout(
        strokeScale = strokeScale,
        onStrokeScaleChange = onStrokeScaleChange,
        strokeColor = strokeColor,
        onStrokeColorChange = onStrokeColorChange,
        gridSize = gridSize,
        onGridSizeChange = onGridSizeChange,
        cellSize = cellSize,
        onCellSizeChange = onCellSizeChange,
        isSettingsVisible = true,
        horizontalSnapPosition = SnapPosition.NONE,
        verticalSnapPosition = SnapPosition.NONE,
        onAreSettingsVisibleChange = {}
    )
}