package com.github.iojjj.bootstrap.qatoolkit.grid

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun GridModeRoute(
    lifecycleOwner: LifecycleOwner,
    onBackPressedDispatcher: OnBackPressedDispatcher,
    viewModelFactory: ViewModelProvider.Factory,
) {
    val viewModel = viewModel<GridModeViewModel>(factory = viewModelFactory)
    LaunchedEffect(Unit) {
        onBackPressedDispatcher.addCallback(lifecycleOwner, viewModel.onBackPressedCallback)
    }
    val strokeScaleState = viewModel.strokeScale.collectAsState()
    val strokeColorState = viewModel.strokeColor.collectAsState()
    val gridSizeState = viewModel.gridSize.collectAsState()
    val cellSizeState = viewModel.cellSize.collectAsState()
    val horizontalSnapPositionState = viewModel.horizontalSnapPosition.collectAsState()
    val verticalSnapPositionState = viewModel.verticalSnapPosition.collectAsState()
    val areSettingsShownState = viewModel.areSettingsShown.collectAsState()
    GridModeLayout(
        strokeScale = strokeScaleState.value,
        onStrokeScaleChange = viewModel.onStrokeScaleChange,
        strokeColor = strokeColorState.value,
        onStrokeColorChange = viewModel.onStrokeColorChange,
        gridSize = gridSizeState.value,
        onGridSizeChange = viewModel.onGridSizeChange,
        cellSize = cellSizeState.value,
        onCellSizeChange = viewModel.onCellSizeChange,
        horizontalSnapPosition = horizontalSnapPositionState.value,
        verticalSnapPosition = verticalSnapPositionState.value,
        isSettingsVisible = areSettingsShownState.value,
        onAreSettingsVisibleChange = viewModel.onAreSettingsShownChange,
    )
}