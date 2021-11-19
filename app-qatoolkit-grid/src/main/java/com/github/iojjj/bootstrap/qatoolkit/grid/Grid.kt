package com.github.iojjj.bootstrap.qatoolkit.grid

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalDpFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.input.pointer.detectTransformGestures
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

private val REGULAR_STROKE_WIDTH = 1.dp
private val SPECIAL_STROKE_WIDTH = 2.dp

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun Grid(
    strokeScale: Float,
    strokeColor: Color,
    gridSize: Int,
    horizontalSnapPosition: SnapPosition = SnapPosition.NONE,
    verticalSnapPosition: SnapPosition = SnapPosition.NONE,
    cellSize: Int,
    minCellSize: Int,
    maxCellSize: Int,
    cellSizeChangeScaleRatio: Float,
    onCellSizeChange: (newCellSize: Int) -> Unit,
) {
    require(minCellSize > 0) {
        "Min cell size must be greater than zero."
    }
    require(minCellSize <= maxCellSize) {
        "Max cell size must be greater or equal to min cell size"
    }

    val density = LocalDensity.current
    val cellSizeState = with(density) {
        rememberUpdatedState(cellSize.dp.toPx())
    }
    val extraCanvasState = with(density) {
        rememberUpdatedState((cellSize * gridSize).dp.toPx())
    }

    val translationXState = remember { mutableStateOf(0f) }
    val translationYState = remember { mutableStateOf(0f) }
    val cellSizeChangedState = remember { mutableStateOf(false) }

    val drawGrid = rememberDrawGrid(
        strokeScale,
        strokeColor,
        gridSize,
        extraCanvasState,
        cellSizeState,
        translationXState,
        translationYState
    )

    val handleGestures = rememberGestureHandler(
        minCellSize,
        maxCellSize,
        horizontalSnapPosition,
        verticalSnapPosition,
        cellSizeChangeScaleRatio,
        onCellSizeChange,
        translationXState,
        translationYState,
        cellSizeState,
        extraCanvasState,
        cellSizeChangedState
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
            .pointerInput(Unit, handleGestures)
            .drawBehind(drawGrid)
    ) {
        CellSize(cellSize, cellSizeChangedState)

        with(density) {
            LaunchedEffect(horizontalSnapPosition, verticalSnapPosition) {
                // Snap at the end of gesture.
                translationYState.value = snap(translationYState.value, maxWidth.toPx(), cellSizeState.value, horizontalSnapPosition)
                translationYState.value = snap(translationYState.value, maxHeight.toPx(), cellSizeState.value, verticalSnapPosition)
            }
        }
    }
}

@Composable
private fun rememberGestureHandler(
    minCellSize: Int,
    maxCellSize: Int,
    horizontalSnapPosition: SnapPosition,
    verticalSnapPosition: SnapPosition,
    cellSizeChangeScaleRatio: Float,
    onCellSizeChange: (Int) -> Unit,
    translationXState: MutableState<Float>,
    translationYState: MutableState<Float>,
    cellSizeState: State<Float>,
    extraCanvasState: State<Float>,
    cellSizeChangedState: MutableState<Boolean>
): suspend PointerInputScope.() -> Unit {
    val minScale = 1.0f
    val horizontalSnapPositionState = rememberUpdatedState(horizontalSnapPosition)
    val verticalSnapPositionState = rememberUpdatedState(verticalSnapPosition)
    val maxScaleState = rememberUpdatedState(1.0f * maxCellSize / minCellSize)
    val scaleStepState = rememberUpdatedState((maxScaleState.value - minScale) / (maxCellSize - minCellSize))
    val cellSizeChangeScaleRatioState = rememberUpdatedState(cellSizeChangeScaleRatio)
    val onCellSizeChangeState = rememberUpdatedState(onCellSizeChange)
    val minCellSizeState = rememberUpdatedState(minCellSize)
    val handleGestures = remember<suspend PointerInputScope.() -> Unit> {
        {
            var cellSizeScale = 1.0f
            detectTransformGestures(
                panZoomLock = true,
                onGestureEnd = {
                    // Snap at the end of gesture.
                    translationXState.value = snap(translationXState.value, size.width.toFloat(), cellSizeState.value, horizontalSnapPositionState.value)
                    translationYState.value = snap(translationYState.value, size.height.toFloat(), cellSizeState.value, verticalSnapPositionState.value)
                    // Remove any extra scale.
                    cellSizeScale -= cellSizeScale % scaleStepState.value
                },
                onGesture = { _, _, pan, zoom, _ ->
                    if (pan != Offset.Zero) {
                        translationXState.value = (translationXState.value + pan.x) % extraCanvasState.value
                        translationYState.value = (translationYState.value + pan.y) % extraCanvasState.value
                    }
                    if (zoom != 1.0f) {
                        val old = cellSizeScale - cellSizeScale % scaleStepState.value
                        val zoomChange = (zoom - 1) * cellSizeChangeScaleRatioState.value
                        val scaledZoom = 1 + zoomChange
                        cellSizeScale = (cellSizeScale * scaledZoom).coerceIn(minScale, maxScaleState.value)
                        val new = cellSizeScale - cellSizeScale % scaleStepState.value
                        if (old != new) {
                            val newCellSize = minCellSizeState.value * new
                            onCellSizeChangeState.value.invoke(newCellSize.roundToInt())
                        }
                        cellSizeChangedState.value = true
                    }
                }
            )
        }
    }
    return handleGestures
}

@Composable
private fun rememberDrawGrid(
    strokeScale: Float,
    strokeColor: Color,
    gridSize: Int,
    extraCanvasState: State<Float>,
    cellSizeState: State<Float>,
    translationXState: MutableState<Float>,
    translationYState: MutableState<Float>,
): DrawScope.() -> Unit {
    val strokeScaleState = rememberUpdatedState(strokeScale)
    val strokeColorState = rememberUpdatedState(strokeColor)
    val gridSizeState = rememberUpdatedState(gridSize)
    return remember {
        {
            val regularStrokeWidth = REGULAR_STROKE_WIDTH.toPx() * strokeScaleState.value
            val specialStrokeWidth = SPECIAL_STROKE_WIDTH.toPx() * strokeScaleState.value
            val extraCanvas = extraCanvasState.value
            val extraWidth = size.width + extraCanvas
            val extraHeight = size.height + extraCanvas
            val maxDimension = size.maxDimension + extraCanvas
            val step = cellSizeState.value

            translate(left = translationXState.value, top = translationYState.value) {
                var i = -extraCanvas
                var cellIndex = 0
                while (i < maxDimension) {
                    i += step
                    cellIndex++
                    val strokeWidth = if (cellIndex.rem(gridSizeState.value) == 0) {
                        specialStrokeWidth
                    } else {
                        regularStrokeWidth
                    }
                    if (i <= extraWidth) {
                        drawLine(strokeColorState.value, Offset(i, -extraCanvas), Offset(i, extraHeight), strokeWidth)
                    }
                    if (i < extraHeight) {
                        drawLine(strokeColorState.value, Offset(-extraCanvas, i), Offset(extraWidth, i), strokeWidth)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun CellSize(
    cellSize: Int,
    cellSizeChangedState: MutableState<Boolean>
) {
    val overlayAlphaState = animateFloatAsState(if (cellSizeChangedState.value) 1.0f else 0.0f)
    val overlayColor = Color.Black.copy(alpha = overlayAlphaState.value * 0.5f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(overlayColor)
            .padding(16.dp)
    ) {
        val dpFormatter = LocalDpFormatter.current
        Text(
            text = dpFormatter(cellSize.toFloat()),
            fontSize = 16.sp,
            color = Color.White.copy(alpha = overlayAlphaState.value),
            modifier = Modifier.align(Alignment.Center)
        )

        if (cellSizeChangedState.value) {
            LaunchedEffect(cellSize) {
                delay(seconds(2))
                cellSizeChangedState.value = false
            }
        }
    }
}

@Stable
private fun snap(
    property: Float,
    propertyMax: Float,
    cellSize: Float,
    snapPosition: SnapPosition,
): Float {
    return when (snapPosition) {
        SnapPosition.NONE -> {
            property
        }
        SnapPosition.START -> {
            property - property.rem(cellSize)
        }
        SnapPosition.END -> {
            property - property.rem(cellSize) - cellSize + propertyMax.rem(cellSize)
        }
    }
}

@Preview
@Composable
private fun PreviewGrid() {
    val (cellSize, onCellSizeChange) = remember { mutableStateOf(8) }
    Grid(
        strokeScale = 1.0f,
        strokeColor = Color.Red.copy(alpha = 0.5f),
        gridSize = 5,
        cellSize = cellSize,
        onCellSizeChange = onCellSizeChange,
        minCellSize = 8,
        maxCellSize = 32,
        cellSizeChangeScaleRatio = 0.5f,
    )
}
