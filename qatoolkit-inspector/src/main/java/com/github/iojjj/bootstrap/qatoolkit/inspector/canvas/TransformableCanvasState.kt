package com.github.iojjj.bootstrap.qatoolkit.inspector.canvas

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import com.github.iojjj.bootstrap.qatoolkit.inspector.canvas.TransformableCanvasState.Companion.TransformableCanvasState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class TransformableCanvasState private constructor(
    private val coroutineScope: CoroutineScope,
    private var zoomRange: ClosedFloatingPointRange<Float>,
    internal val viewportState: MutableState<Rect>,
    canvasSizeState: MutableState<IntSize>,
    private val transformableDelegate: TransformableDelegate,
) : Transformable by transformableDelegate {

    private var viewport by viewportState
    private var canvasSize by canvasSizeState
    private var viewportFocus = Offset.Unspecified
    private val animatedZoomState = Animatable(zoomRange.start)
    private val zoomState = mutableStateOf(zoomRange.start)

    val zoom: Float
        @Stable
        get() = zoomState.value

    val isZooming: Boolean
        @Stable
        get() = animatedZoomState.isRunning

    @SuppressLint("ComposableNaming")
    @Composable
    internal fun animateZoomChanges() {
        val animatedZoom = animatedZoomState.value
        LaunchedEffect(animatedZoom) {
            if (animatedZoom != zoomState.value) {
                zoomViewport(animatedZoom)
            }
        }
    }

    fun animateZoomBy(
        delta: Float,
        focus: Offset = Offset.Unspecified,
    ) {
        val coercedZoom = (animatedZoomState.value + delta).coerceIn(zoomRange)
        if (coercedZoom != zoomState.value) {
            updateViewportFocus(focus)
            coroutineScope.launch {
                animatedZoomState.snapTo(zoomState.value)
                animatedZoomState.animateTo(coercedZoom)
            }
        }
    }

    fun animateZoomTo(
        newZoom: Float,
        focus: Offset = Offset.Unspecified,
    ) {
        val coercedZoom = newZoom.coerceIn(zoomRange)
        if (coercedZoom != zoomState.value) {
            updateViewportFocus(focus)
            coroutineScope.launch {
                animatedZoomState.snapTo(zoomState.value)
                animatedZoomState.animateTo(coercedZoom)
            }
        }
    }

    fun zoomBy(
        delta: Float,
        focus: Offset = Offset.Unspecified,
    ) {
        val coercedZoom = (animatedZoomState.value + delta).coerceIn(zoomRange)
        if (coercedZoom != zoomState.value) {
            if (isZooming) {
                coroutineScope.launch {
                    animatedZoomState.snapTo(zoomState.value)
                }
            }
            updateViewportFocus(focus)
            zoomViewport(coercedZoom)
        }
    }

    fun zoomTo(
        newZoom: Float,
        focus: Offset = Offset.Unspecified,
    ) {
        val coercedZoom = newZoom.coerceIn(zoomRange)
        if (coercedZoom != zoomState.value) {
            if (isZooming) {
                coroutineScope.launch {
                    animatedZoomState.snapTo(zoomState.value)
                }
            }
            updateViewportFocus(focus)
            zoomViewport(coercedZoom)
        }
    }

    fun panBy(delta: Offset) {
        val viewportDelta = transformableDelegate.toViewportDelta(-delta)

        val center = viewport.center
        val viewportHalfSize = viewport.size / 2.0f
        viewportFocus = Offset(
            (center.x + viewportDelta.x).coerceIn(AXIS_X_MIN + viewportHalfSize.width, AXIS_X_MAX - viewportHalfSize.width),
            (center.y + viewportDelta.y).coerceIn(AXIS_Y_MIN + viewportHalfSize.height, AXIS_Y_MAX - viewportHalfSize.height)
        )
        viewport = Rect(
            topLeft = Offset(viewportFocus.x - viewportHalfSize.width, viewportFocus.y - viewportHalfSize.height),
            bottomRight = Offset(viewportFocus.x + viewportHalfSize.width, viewportFocus.y + viewportHalfSize.height),
        )
    }

    private fun updateViewportFocus(focus: Offset) {
        if (focus != Offset.Unspecified) {
            viewportFocus = Offset(
                viewport.left + focus.x * viewport.width / canvasSize.width,
                viewport.top + focus.y * viewport.height / canvasSize.height,
            )
        }
    }

    private fun zoomViewport(newZoom: Float) {
        if (newZoom == 1.0f) {
            viewport = Rect(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX)
            viewportFocus = viewport.center
            zoomState.value = 1.0f
            return
        }

        val newWidth = AXIS_X_LENGTH / newZoom
        val newHeight = AXIS_Y_LENGTH / newZoom
        val pointWithinViewport = Offset(
            (viewportFocus.x - viewport.left) / viewport.width,
            (viewportFocus.y - viewport.top) / viewport.height,
        )
        val topLeft = Offset(
            viewportFocus.x - newWidth * pointWithinViewport.x,
            viewportFocus.y - newHeight * pointWithinViewport.y,
        )

        viewport = Rect(
            left = topLeft.x.coerceAtLeast(AXIS_X_MIN),
            top = topLeft.y.coerceAtLeast(AXIS_Y_MIN),
            right = (topLeft.x + newWidth).coerceAtMost(AXIS_X_MAX),
            bottom = (topLeft.y + newHeight).coerceAtMost(AXIS_Y_MAX),
        )
        zoomState.value = newZoom
    }

    internal fun onCanvasSizeChanged(size: IntSize) {
        canvasSize = size
    }

    companion object {

        fun TransformableCanvasState(
            coroutineScope: CoroutineScope,
            minZoom: Float,
            maxZoom: Float,
        ): TransformableCanvasState {
            require(minZoom >= 1.0f) {
                "Min zoom must be greater or equals to 1.0."
            }
            require(maxZoom >= minZoom) {
                "Max zoom can't be less then min zoom."
            }
            val viewportState = mutableStateOf(Rect(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX))
            val canvasSizeState = mutableStateOf(IntSize.Zero)
            return TransformableCanvasState(
                coroutineScope,
                minZoom..maxZoom,
                viewportState,
                canvasSizeState,
                TransformableDelegate(viewportState, canvasSizeState)
            )
        }
    }

}

@Composable
fun rememberTransformableCanvasState(
    maxZoom: Float,
): TransformableCanvasState {
    val coroutineScope = rememberCoroutineScope()
    return remember {
        TransformableCanvasState(
            coroutineScope = coroutineScope,
            minZoom = 1.0f,
            maxZoom = maxZoom
        )
    }
}