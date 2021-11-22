package com.github.iojjj.bootstrap.qatoolkit.ruler

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalPercentFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalScreenSize
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalSizeFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope.vertically
import com.github.iojjj.bootstrap.qatoolkit.core.DimensionType
import com.github.iojjj.bootstrap.qatoolkit.core.Orientation
import com.github.iojjj.bootstrap.qatoolkit.settings.rememberScaleFormatter
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

internal const val OFFSET_START = 0.0f
internal const val OFFSET_END = 1.0f
internal const val OFFSET_RANGE = OFFSET_END - OFFSET_START

@OptIn(ExperimentalTime::class, ExperimentalAnimationApi::class)
@Composable
fun Ruler(
    orientation: Orientation,
    onOrientationChange: (Orientation) -> Unit,
    unpinnedPosition: Float,
    onUnpinnedPositionChange: (Float) -> Unit,
    pinnedPositions: List<Float>,
    strokeScale: Float,
    strokeColor: Color,
    textScale: Float,
    textColor: Color,
    sensitivity: Float,
    onSensitivityChange: (Float) -> Unit,
    minSensitivity: Float,
    maxSensitivity: Float,
    isPercentVisible: Boolean,
    dimensionType: DimensionType,
    numberFormatter: (Float) -> String = LocalSizeFormatter.current,
    percentFormatter: (Float) -> String = LocalPercentFormatter.current,
) {
    val sensitivityChangedState = remember {
        mutableStateOf(false)
    }
    val orientationState = rememberUpdatedState(orientation)
    val unpinnedPositionState = rememberUpdatedState(unpinnedPosition)
    val layoutDirectionState = rememberLayoutDirectionState()
    val onSensitivityChangeState = rememberUpdatedState(onSensitivityChange)
    val localOnSensitivityChangeState = remember<(Float) -> Unit> {
        {
            onSensitivityChangeState.value.invoke(it)
            sensitivityChangedState.value = true
        }
    }
    val pointerInputHandler = rememberPointerInputHandler(
        orientationState,
        onOrientationChange,
        unpinnedPositionState,
        onUnpinnedPositionChange,
        sensitivity,
        localOnSensitivityChangeState,
        minSensitivity,
        maxSensitivity,
        layoutDirectionState,
    )
    val rulerLinesDrawer = rememberRulerLinesDrawer(
        pinnedPositions,
        onUnpinnedPositionChange,
        strokeScale,
        strokeColor,
        unpinnedPositionState
    )
    val screenSize = LocalScreenSize.current
    val screenSizeState = rememberUpdatedState(
        when (orientation) {
            Orientation.HORIZONTAL -> screenSize
            Orientation.VERTICAL -> IntSize(screenSize.height, screenSize.width)
        }
    )
    val rulerTextDrawer = rememberRulerTextDrawer(
        textColor,
        numberFormatter,
        percentFormatter,
        isPercentVisible,
        textScale,
        dimensionType,
        layoutDirectionState,
        unpinnedPositionState,
        screenSizeState,
    )
    val onDraw = rememberOnDraw(
        orientationState,
        rulerLinesDrawer,
        rulerTextDrawer
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit, pointerInputHandler)
            .drawBehind(onDraw)
    ) {
        Sensitivity(sensitivity, sensitivityChangedState)
    }

    if (sensitivityChangedState.value) {
        LaunchedEffect(sensitivity) {
            delay(seconds(2))
            sensitivityChangedState.value = false
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun Sensitivity(
    sensitivity: Float,
    sensitivityChangedState: MutableState<Boolean>
) {
    val overlayAlphaState = animateFloatAsState(if (sensitivityChangedState.value) 1.0f else 0.0f)
    val overlayColor = Color.Black.copy(alpha = overlayAlphaState.value * 0.5f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(overlayColor)
            .padding(16.dp)
    ) {
        val scaleFormatter = rememberScaleFormatter()
        Text(
            text = scaleFormatter(sensitivity),
            fontSize = 16.sp,
            color = Color.White.copy(alpha = overlayAlphaState.value),
            modifier = Modifier.align(Alignment.Center)
        )

        if (sensitivityChangedState.value) {
            LaunchedEffect(sensitivity) {
                delay(seconds(2))
                sensitivityChangedState.value = false
            }
        }
    }
}

@Composable
private fun rememberOnDraw(
    orientationState: State<Orientation>,
    rulerLinesDrawer: RulerLinesDrawer,
    rulerTextDrawer: RulerTextDrawer,
): DrawScope.() -> Unit {
    return remember {
        {
            withOrientation(orientationState.value) {
                // Draw rulers
                val points = rulerLinesDrawer.draw(this)
                // Draw text
                rulerTextDrawer.draw(this, points)
            }
        }
    }
}

@Composable
private fun rememberLayoutDirectionState(): Animatable<Float, AnimationVector1D> {
    val layoutDirection = LocalLayoutDirection.current
    val layoutDirectionState = remember {
        Animatable(
            when (layoutDirection) {
                LayoutDirection.Ltr -> OFFSET_START
                LayoutDirection.Rtl -> OFFSET_END
            }
        )
    }
    LaunchedEffect(layoutDirection) {
        layoutDirectionState.animateTo(
            when (layoutDirection) {
                LayoutDirection.Ltr -> OFFSET_START
                LayoutDirection.Rtl -> OFFSET_END
            }
        )
    }
    return layoutDirectionState
}

private fun DrawScope.withOrientation(
    orientation: Orientation,
    onDraw: DrawScope.() -> Unit,
) {
    exhaustive..when (orientation) {
        Orientation.HORIZONTAL -> {
            onDraw()
        }
        Orientation.VERTICAL -> {
            vertically(onDraw)
        }
    }
}

@Preview
@Composable
fun Preview() {
    val (unpinnedPosition, onUnpinnedPositionChange) = remember { mutableStateOf(Float.NaN) }
    val (sensitivity, onSensitivityChange) = remember { mutableStateOf(1.0f) }
    Ruler(
        orientation = Orientation.HORIZONTAL,
        onOrientationChange = {},
        unpinnedPosition = unpinnedPosition,
        onUnpinnedPositionChange = onUnpinnedPositionChange,
        pinnedPositions = listOf(),
        strokeScale = 1.0f,
        strokeColor = Color.Red,
        textScale = 1.0f,
        textColor = Color.White,
        sensitivity = sensitivity,
        onSensitivityChange = onSensitivityChange,
        isPercentVisible = false,
        dimensionType = DimensionType.DP,
        minSensitivity = 0.1f,
        maxSensitivity = 1.0f,
    )
}