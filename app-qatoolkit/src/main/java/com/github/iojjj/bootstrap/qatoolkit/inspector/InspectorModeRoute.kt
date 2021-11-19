package com.github.iojjj.bootstrap.qatoolkit.inspector

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.iojjj.bootstrap.qatoolkit.inspector.canvas.rememberTransformableCanvasState

@Composable
internal fun InspectorModeRoute(
    findRootViewNode: () -> ViewNode?,
    lifecycleOwner: LifecycleOwner,
    onBackPressedDispatcher: OnBackPressedDispatcher,
    viewModelFactory: ViewModelProvider.Factory,
) {
    val viewModel = viewModel<InspectorModeViewModel>(factory = viewModelFactory)
    val transformableCanvasState = rememberTransformableCanvasState(10.0f)
    val onBackPressedZoomOut = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                transformableCanvasState.animateZoomTo(1.0f)
            }
        }
    }
    LaunchedEffect(Unit) {
        onBackPressedDispatcher.addCallback(lifecycleOwner, onBackPressedZoomOut)
        onBackPressedDispatcher.addCallback(lifecycleOwner, viewModel.onBackPressedCallback)
        viewModel.updateState(findRootViewNode())
    }
    val zoom = transformableCanvasState.zoom
    LaunchedEffect(zoom) {
        onBackPressedZoomOut.isEnabled = zoom > 1.0f
    }

    val rootNodeState = viewModel.rootNode.collectAsState()
    val selectedNodeState = viewModel.selectedNode.collectAsState()
    val pinnedNodeState = viewModel.pinnedNode.collectAsState()
    val nodeStrokeScaleState = viewModel.nodeStrokeScale.collectAsState()
    val overlayColorState = viewModel.overlayColor.collectAsState()
    val nodeStrokeColorState = viewModel.nodeStrokeColor.collectAsState()
    val selectedStrokeColorState = viewModel.selectedStrokeColor.collectAsState()
    val pinnedStrokeColorState = viewModel.pinnedStrokeColor.collectAsState()
    val arrowColorState = viewModel.arrowColor.collectAsState()
    val textScaleState = viewModel.textScale.collectAsState()
    val textColorState = viewModel.textColor.collectAsState()
    val dimensionTypeState = viewModel.dimensionType.collectAsState()
    val areSettingsShownState = viewModel.areSettingsShown.collectAsState()
    val isPercentVisibleState = viewModel.isPercentVisible.collectAsState()

    InspectorModeLayout(
        rootNode = rootNodeState.value,
        selectedNode = selectedNodeState.value,
        pinnedNode = pinnedNodeState.value,
        transformableCanvasState = transformableCanvasState,
        nodeStrokeScale = nodeStrokeScaleState.value,
        onNodeStrokeScaleChange = viewModel.onNodeStrokeScaleChange,
        overlayColor = overlayColorState.value,
        onOverlayColorChange = viewModel.onOverlayColorChange,
        nodeStrokeColor = nodeStrokeColorState.value,
        onNodeStrokeColorChange = viewModel.onNodeStrokeColorChange,
        selectedStrokeColor = selectedStrokeColorState.value,
        onSelectedStrokeColorChange = viewModel.onSelectedStrokeColorChange,
        pinnedStrokeColor = pinnedStrokeColorState.value,
        onPinnedStrokeColorChange = viewModel.onPinnedStrokeColorChange,
        arrowColor = arrowColorState.value,
        onArrowColorChange = viewModel.onArrowColorChange,
        textScale = textScaleState.value,
        onTextScaleChange = viewModel.onTextScaleChange,
        textColor = textColorState.value,
        onTextColorChange = viewModel.onTextColorChange,
        dimensionType = dimensionTypeState.value,
        isPercentVisible = isPercentVisibleState.value,
        areSettingsShown = areSettingsShownState.value,
        onAreSettingsShownChange = viewModel.onAreSettingsShownChange,
        onNodeSelectionRequested = viewModel.onNodeSelectionRequested,
    )

    NodeDetails(viewModel)
}

