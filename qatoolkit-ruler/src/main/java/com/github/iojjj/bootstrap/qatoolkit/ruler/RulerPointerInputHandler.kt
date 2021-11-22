package com.github.iojjj.bootstrap.qatoolkit.ruler

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import com.github.iojjj.bootstrap.qatoolkit.compose.core.input.pointer.detectTransformGestures
import com.github.iojjj.bootstrap.qatoolkit.core.Orientation
import kotlin.math.abs

internal class RulerPointerInputHandler(
    orientationState: State<Orientation>,
    onOrientationChangeState: State<(Orientation) -> Unit>,
    sensitivityState: State<Float>,
    onSensitivityChangeState: State<(Float) -> Unit>,
    minSensitivityState: State<Float>,
    maxSensitivityState: State<Float>,
    horizontalDragHandlerState: State<DragGestureHandler>,
    verticalDragHandlerState: State<DragGestureHandler>,
) {

    private val orientation by orientationState
    private val onOrientationChange by onOrientationChangeState
    private val sensitivity by sensitivityState
    private val onSensitivityChange by onSensitivityChangeState
    private val minSensitivity by minSensitivityState
    private val maxSensitivity by maxSensitivityState
    private val horizontalDragHandler by horizontalDragHandlerState
    private val verticalDragHandler by verticalDragHandlerState

    private var dragOrientation: Orientation? = null
    private var rotationOffset = 0.0f
    private var rotationThreshold = ROTATION_THRESHOLD_INITIAL

    val handle: suspend PointerInputScope.() -> Unit = {
        detectTransformGestures(
            panZoomLock = true,
            onGesture = { change, _, pan, zoom, rotation ->
                if (rotation != 0.0f) {
                    rotationOffset += rotation
                    if (abs(rotationOffset) > rotationThreshold) {
                        rotationOffset = 0.0f
                        rotationThreshold = ROTATION_THRESHOLD_NEXT
                        onOrientationChange(
                            when (orientation) {
                                Orientation.HORIZONTAL -> Orientation.VERTICAL
                                Orientation.VERTICAL -> Orientation.HORIZONTAL
                            }
                        )
                    }
                } else if (zoom != 1.0f) {
                    val sensitivity = (sensitivity * zoom).coerceIn(minSensitivity, maxSensitivity)
                    onSensitivityChange(sensitivity)
                } else if (pan != Offset.Zero) {
                    val orientation = dragOrientation
                        ?: findDragOrientation(pan).also {
                            dragOrientation = it
                            val onDragStart = when (dragOrientation) {
                                Orientation.HORIZONTAL -> horizontalDragHandler.onDragStart
                                Orientation.VERTICAL -> verticalDragHandler.onDragStart
                                null -> null
                            }
                            onDragStart?.invoke(this)
                        }
                    when (orientation) {
                        Orientation.HORIZONTAL -> {
                            horizontalDragHandler.onDrag(this, change, pan.x)
                        }
                        Orientation.VERTICAL -> {
                            verticalDragHandler.onDrag(this, change, pan.y)
                        }
                        null -> {
                            /* no-op */
                        }
                    }
                }
            },
            onGestureEnd = {
                val onDragEnd = when (dragOrientation) {
                    Orientation.HORIZONTAL -> horizontalDragHandler.onDragEnd
                    Orientation.VERTICAL -> verticalDragHandler.onDragEnd
                    null -> null
                }
                onDragEnd?.invoke(this)
                dragOrientation = null
                rotationOffset = 0.0f
                rotationThreshold = ROTATION_THRESHOLD_INITIAL
            },
        )
    }

    private fun findDragOrientation(
        pan: Offset,
    ): Orientation? {
        val max = maxOf(abs(pan.x), abs(pan.y))
        val fractionX = abs(pan.x / max)
        val fractionY = abs(pan.y / max)
        return when {
            abs(fractionX - fractionY) < 0.5f -> null
            fractionX == 1.0f -> Orientation.HORIZONTAL
            fractionY == 1.0f -> Orientation.VERTICAL
            else -> null
        }
    }

    companion object {

        private const val ROTATION_THRESHOLD_INITIAL = 45.0f
        private const val ROTATION_THRESHOLD_NEXT = 90.0f
    }
}

@Composable
internal fun rememberPointerInputHandler(
    orientationState: State<Orientation>,
    onOrientationChange: (Orientation) -> Unit,
    unpinnedPositionState: State<Float>,
    onUnpinnedPositionChange: (Float) -> Unit,
    sensitivity: Float,
    onSensitivityChange: (Float) -> Unit,
    minSensitivity: Float,
    maxSensitivity: Float,
    layoutDirectionState: Animatable<Float, AnimationVector1D>,
): suspend PointerInputScope.() -> Unit {

    val onOrientationChangeState = rememberUpdatedState(onOrientationChange)
    val onUnpinnedPositionChangeState = rememberUpdatedState(onUnpinnedPositionChange)
    val sensitivityState = rememberUpdatedState(sensitivity)
    val onSensitivityChangeState = rememberUpdatedState(onSensitivityChange)
    val minSensitivityState = rememberUpdatedState(minSensitivity)
    val maxSensitivityState = rememberUpdatedState(maxSensitivity)
    val unpinnedPositionGestureHandler = remember {
        UnpinnedPositionHandler(orientationState, unpinnedPositionState, onUnpinnedPositionChangeState, sensitivityState)
    }
    val coroutineScope = rememberCoroutineScope()
    val layoutDirectionOffsetGestureHandler = remember(coroutineScope) {
        LayoutDirectionOffsetHandler(coroutineScope, layoutDirectionState, orientationState)
    }
    val verticalDragGestureHandler = rememberUpdatedState(
        when (orientationState.value) {
            Orientation.HORIZONTAL -> unpinnedPositionGestureHandler
            Orientation.VERTICAL -> layoutDirectionOffsetGestureHandler
        }
    )
    val horizontalDragGestureHandler = rememberUpdatedState(
        when (orientationState.value) {
            Orientation.HORIZONTAL -> layoutDirectionOffsetGestureHandler
            Orientation.VERTICAL -> unpinnedPositionGestureHandler
        }
    )
    return remember {
        RulerPointerInputHandler(
            orientationState,
            onOrientationChangeState,
            sensitivityState,
            onSensitivityChangeState,
            minSensitivityState,
            maxSensitivityState,
            horizontalDragGestureHandler,
            verticalDragGestureHandler
        ).handle
    }
}