package com.github.iojjj.bootstrap.qatoolkit.inspector

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.iojjj.bootstrap.qatoolkit.core.DimensionType
import com.github.iojjj.bootstrap.qatoolkit.inspector.canvas.TransformableCanvasState

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun InspectorModeLayout(
    rootNode: Node?,
    transformableCanvasState: TransformableCanvasState,
    pinnedNode: Node?,
    selectedNode: Node?,
    overlayColor: Color,
    onOverlayColorChange: (Color) -> Unit,
    arrowColor: Color,
    onArrowColorChange: (Color) -> Unit,
    textScale: Float,
    onTextScaleChange: (Float) -> Unit,
    textColor: Color,
    onTextColorChange: (Color) -> Unit,
    nodeStrokeScale: Float,
    onNodeStrokeScaleChange: (Float) -> Unit,
    nodeStrokeColor: Color,
    onNodeStrokeColorChange: (Color) -> Unit,
    pinnedStrokeColor: Color,
    onPinnedStrokeColorChange: (Color) -> Unit,
    selectedStrokeColor: Color,
    onSelectedStrokeColorChange: (Color) -> Unit,
    dimensionType: DimensionType,
    isPercentVisible: Boolean,
    areSettingsShown: Boolean,
    onAreSettingsShownChange: (Boolean) -> Unit,
    onNodeSelectionRequested: (Offset) -> Unit,
) {
    Box {
        if (rootNode == null) {
            NoHierarchy()
        } else {
            val onNodeSelectionRequestedState = rememberUpdatedState(onNodeSelectionRequested)
            Inspector(
                rootNode = rootNode,
                transformableCanvasState = transformableCanvasState,
                pinnedNode = pinnedNode,
                selectedNode = selectedNode,
                overlayColor = overlayColor,
                arrowColor = arrowColor,
                textScale = textScale,
                textColor = textColor,
                nodeStrokeScale = nodeStrokeScale,
                nodeStrokeColor = nodeStrokeColor,
                pinnedStrokeColor = pinnedStrokeColor,
                selectedStrokeColor = selectedStrokeColor,
                dimensionType = dimensionType,
                isPercentVisible = isPercentVisible,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            onNodeSelectionRequestedState.value.invoke(transformableCanvasState.fromDrawOffset(it))
                        }
                    }
            )

            val onAreSettingsShownChangeState = rememberUpdatedState(onAreSettingsShownChange)
            AnimatedVisibility(
                visible = areSettingsShown,
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
                        onAreSettingsShownChangeState.value.invoke(it == BottomSheetValue.Expanded)
                    }
                }

                InspectorSettingsPanel(
                    overlayColor,
                    onOverlayColorChange,
                    arrowColor,
                    onArrowColorChange,
                    nodeStrokeScale,
                    onNodeStrokeScaleChange,
                    nodeStrokeColor,
                    onNodeStrokeColorChange,
                    selectedStrokeColor,
                    onSelectedStrokeColorChange,
                    pinnedStrokeColor,
                    onPinnedStrokeColorChange,
                    textScale,
                    onTextScaleChange,
                    textColor,
                    onTextColorChange,
                    onBottomSheetStateChange = onBottomSheetStateChange
                )
            }
        }
    }
}

@Composable
private fun NoHierarchy() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.error)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.qatoolkit_inspector_hierarchy_not_captured),
            color = MaterialTheme.colors.onError,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}