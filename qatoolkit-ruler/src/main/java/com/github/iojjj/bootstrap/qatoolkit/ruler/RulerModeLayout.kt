package com.github.iojjj.bootstrap.qatoolkit.ruler

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
import com.github.iojjj.bootstrap.qatoolkit.core.DimensionType
import com.github.iojjj.bootstrap.qatoolkit.core.Orientation

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun RulerModeLayout(
    orientation: Orientation,
    onOrientationChange: (Orientation) -> Unit,
    unpinnedPosition: Float,
    onUnpinnedPositionChange: (Float) -> Unit,
    pinnedPositions: List<Float>,
    isPercentVisible: Boolean,
    dimensionType: DimensionType,
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
    isSettingsVisible: Boolean,
    onAreSettingsVisibleChange: (Boolean) -> Unit,
) {
    val onAreSettingsVisibleChangeState = rememberUpdatedState(onAreSettingsVisibleChange)
    Box {
        Ruler(
            orientation = orientation,
            onOrientationChange = onOrientationChange,
            unpinnedPosition = unpinnedPosition,
            onUnpinnedPositionChange = onUnpinnedPositionChange,
            pinnedPositions = pinnedPositions,
            strokeScale = strokeScale,
            strokeColor = strokeColor,
            textScale = textScale,
            textColor = textColor,
            sensitivity = sensitivity,
            onSensitivityChange = onSensitivityChange,
            isPercentVisible = isPercentVisible,
            dimensionType = dimensionType,
            minSensitivity = 0.1f,
            maxSensitivity = 1.0f,
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
                onBottomSheetStateChange = onBottomSheetStateChange
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewGridModeLayout() {
    val (unpinnedPosition, onUnpinnedPositionChange) = remember { mutableStateOf(Float.NaN) }
    val (strokeScale, onStrokeScaleChange) = remember { mutableStateOf(1.0f) }
    val (strokeColor, onStrokeColorChange) = remember { mutableStateOf(Color.Red.copy(alpha = 0.5f)) }
    val (textScale, onTextScaleChange) = remember { mutableStateOf(1.0f) }
    val (textColor, onTextColorChange) = remember { mutableStateOf(Color.White) }
    val (sensitivity, onSensitivityChange) = remember { mutableStateOf(1.0f) }
    RulerModeLayout(
        orientation = Orientation.HORIZONTAL,
        onOrientationChange = {},
        unpinnedPosition = unpinnedPosition,
        onUnpinnedPositionChange = onUnpinnedPositionChange,
        pinnedPositions = emptyList(),
        isPercentVisible = false,
        dimensionType = DimensionType.DP,
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
        isSettingsVisible = true,
        onAreSettingsVisibleChange = { }
    )
}