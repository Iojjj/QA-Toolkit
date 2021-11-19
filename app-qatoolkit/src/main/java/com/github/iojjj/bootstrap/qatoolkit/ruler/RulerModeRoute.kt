package com.github.iojjj.bootstrap.qatoolkit.ruler

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun RulerModeRoute(
    lifecycleOwner: LifecycleOwner,
    onBackPressedDispatcher: OnBackPressedDispatcher,
    viewModelFactory: ViewModelProvider.Factory,
) {
    val viewModel = viewModel<RulerModeViewModel>(factory = viewModelFactory)
    LaunchedEffect(Unit) {
        onBackPressedDispatcher.addCallback(lifecycleOwner, viewModel.onBackPressedCallback)
    }
    val orientationState = viewModel.orientation.collectAsState()
    val unpinnedPositionState = viewModel.unpinnedPosition.collectAsState()
    val pinnedPositionsState = viewModel.pinnedPositions.collectAsState()
    val strokeScaleState = viewModel.strokeScale.collectAsState()
    val strokeColorState = viewModel.strokeColor.collectAsState()
    val textScaleState = viewModel.textScale.collectAsState()
    val textColorState = viewModel.textColor.collectAsState()
    val sensitivityState = viewModel.sensitivity.collectAsState()
    val dimensionTypeState = viewModel.dimensionType.collectAsState()
    val isPercentVisibleState = viewModel.isPercentVisible.collectAsState()
    val areSettingsShownState = viewModel.areSettingsShown.collectAsState()
    RulerModeLayout(
        orientation = orientationState.value,
        onOrientationChange = viewModel.onOrientationChange,
        unpinnedPosition = unpinnedPositionState.value,
        onUnpinnedPositionChange = viewModel.onUnpinnedPositionChange,
        pinnedPositions = pinnedPositionsState.value,
        strokeScale = strokeScaleState.value,
        onStrokeScaleChange = viewModel.onStrokeScaleChange,
        strokeColor = strokeColorState.value,
        onStrokeColorChange = viewModel.onStrokeColorChange,
        textScale = textScaleState.value,
        onTextScaleChange = viewModel.onTextScaleChange,
        textColor = textColorState.value,
        onTextColorChange = viewModel.onTextColorChange,
        sensitivity = sensitivityState.value,
        onSensitivityChange = viewModel.onSensitivityChange,
        dimensionType = dimensionTypeState.value,
        isPercentVisible = isPercentVisibleState.value,
        isSettingsVisible = areSettingsShownState.value,
        onAreSettingsVisibleChange = viewModel.onAreSettingsShownChange,
    )
}