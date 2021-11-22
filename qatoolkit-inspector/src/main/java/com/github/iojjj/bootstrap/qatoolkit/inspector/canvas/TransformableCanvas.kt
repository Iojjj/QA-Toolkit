package com.github.iojjj.bootstrap.qatoolkit.inspector.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

internal const val AXIS_X_MIN = -1.0f
internal const val AXIS_X_MAX = +1.0f
internal const val AXIS_Y_MIN = -1.0f
internal const val AXIS_Y_MAX = +1.0f
internal const val AXIS_X_LENGTH = AXIS_X_MAX - AXIS_X_MIN
internal const val AXIS_Y_LENGTH = AXIS_Y_MAX - AXIS_Y_MIN

@Composable
fun TransformableCanvas(
    modifier: Modifier,
    maxZoom: Float,
    onDraw: TransformableDrawScope.() -> Unit,
) {
    TransformableCanvas(modifier, rememberTransformableCanvasState(maxZoom), onDraw)
}

@Composable
fun TransformableCanvas(
    modifier: Modifier,
    state: TransformableCanvasState,
    onDraw: TransformableDrawScope.() -> Unit,
) {
    val onDrawState = rememberUpdatedState(onDraw)

    val localOnDraw = remember<DrawScope.() -> Unit> {
        val drawScope = TransformableDrawScopeImpl(state);
        {
            drawScope.delegate = this
            onDrawState.value.invoke(drawScope)
        }
    }
    val onSizeChangedState = remember<(IntSize) -> Unit> {
        {
            state.onCanvasSizeChanged(it)
        }
    }

    Canvas(
        onDraw = localOnDraw,
        modifier = modifier
            .onSizeChanged(onSizeChangedState)
            .pointerInput(Unit) {
                detectTransformGestures { centroid, panChange, zoomChange, _ ->
                    if (zoomChange != 1.0f) {
                        state.zoomTo(state.zoom * zoomChange, centroid)
                    } else if (panChange != Offset.Zero) {
                        state.panBy(panChange)
                    }
                }
            }
    )

    state.animateZoomChanges()

}