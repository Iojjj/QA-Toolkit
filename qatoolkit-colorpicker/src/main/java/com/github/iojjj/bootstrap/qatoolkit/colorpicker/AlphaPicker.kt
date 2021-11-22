package com.github.iojjj.bootstrap.qatoolkit.colorpicker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.style.ColorPickerStyle
import kotlin.math.roundToInt

private val SQUARE_SIDE = 8.dp
private val TRIANGLE_SIDE = 6.dp
private const val SQRT_3 = 1.732f // ~sqrt(3)

@Composable
fun HorizontalAlphaPicker(
    state: ColorPickerState,
    modifier: Modifier,
    indicatorColor: Color = ColorPickerStyle.Colors.alphaIndicator,
) {
    val onTapChangeAlpha = remember {
        createOnTapChangeAlpha(state, Offset::x, IntSize::width)
    }
    val onDragChangeAlpha = remember {
        createOnDragChangeAlpha(state, Offset::x, IntSize::width)
    }
    Canvas(
        modifier = modifier
            .pointerInput(Unit, onTapChangeAlpha)
            .pointerInput(Unit, onDragChangeAlpha),
        onDraw = rememberHorizontalAlphaDrawer(state, indicatorColor)
    )
}

@Composable
fun VerticalAlphaPicker(
    state: ColorPickerState,
    modifier: Modifier,
    indicatorColor: Color = ColorPickerStyle.Colors.alphaIndicator,
) {
    val onTapChangeAlpha = remember {
        createOnTapChangeAlpha(state, Offset::y, IntSize::height)
    }
    val onDragChangeAlpha = remember {
        createOnDragChangeAlpha(state, Offset::y, IntSize::height)
    }
    Canvas(
        modifier = modifier
            .pointerInput(Unit, onTapChangeAlpha)
            .pointerInput(Unit, onDragChangeAlpha),
        onDraw = rememberVerticalAlphaDrawer(state, indicatorColor)
    )
}

@OptIn(ExperimentalGraphicsApi::class)
@Composable
private fun rememberHorizontalAlphaDrawer(
    state: ColorPickerState,
    indicatorColor: Color,
): DrawScope.() -> Unit {
    val alphaBrushState = remember {
        mutableStateOf<Brush>(SolidColor(Color.Transparent))
    }
    alphaBrushState.value = remember(state.hue, state.saturation, state.value) {
        verticalGradient(listOf(Color.Transparent, Color.hsv(state.hue, state.saturation, state.value)))
    }
    val path = remember {
        Path()
    }
    val indicatorColorState = rememberUpdatedState(indicatorColor)
    return remember {
        {
            drawTransparentBackground()
            drawRect(alphaBrushState.value)
            val x = state.alpha * size.width
            val halfSide = TRIANGLE_SIDE.toPx() / 2
            path.run {
                reset()
                moveTo(x - halfSide, 0.0f)
                lineTo(x + halfSide, 0.0f)
                lineTo(x, halfSide * SQRT_3)
                close()
                drawPath(this, indicatorColorState.value)
                reset()
                moveTo(x - halfSide, size.height)
                lineTo(x + halfSide, size.height)
                lineTo(x, size.height - halfSide * SQRT_3)
                close()
                drawPath(this, indicatorColorState.value)
            }
        }
    }
}

@OptIn(ExperimentalGraphicsApi::class)
@Composable
private fun rememberVerticalAlphaDrawer(
    state: ColorPickerState,
    indicatorColor: Color,
): DrawScope.() -> Unit {
    val alphaBrushState = remember {
        mutableStateOf<Brush>(SolidColor(Color.Transparent))
    }
    alphaBrushState.value = remember(state.hue, state.saturation, state.value) {
        verticalGradient(listOf(Color.Transparent, Color.hsv(state.hue, state.saturation, state.value)))
    }
    val path = remember {
        Path()
    }
    val indicatorColorState = rememberUpdatedState(indicatorColor)
    return remember {
        {
            drawTransparentBackground()
            drawRect(alphaBrushState.value)
            val y = state.alpha * size.height
            val halfSide = TRIANGLE_SIDE.toPx() / 2
            path.run {
                reset()
                moveTo(0.0f, y - halfSide)
                lineTo(0.0f, y + halfSide)
                lineTo(halfSide * SQRT_3, y)
                close()
                drawPath(this, indicatorColorState.value)
                reset()
                moveTo(size.width, y - halfSide)
                lineTo(size.width, y + halfSide)
                lineTo(size.width - halfSide * SQRT_3, y)
                close()
                drawPath(this, indicatorColorState.value)
            }
        }
    }
}

private fun createOnTapChangeAlpha(
    state: ColorPickerState,
    get: Offset.() -> Float,
    max: IntSize.() -> Int,
): suspend PointerInputScope.() -> Unit = {
    detectTapGestures {
        state.update(alpha = calculateAlpha(it.get(), size.max()))
    }
}

private fun createOnDragChangeAlpha(
    state: ColorPickerState,
    get: Offset.() -> Float,
    max: IntSize.() -> Int,
): suspend PointerInputScope.() -> Unit = {
    var startedAlpha = Float.NaN
    detectDragGestures(
        onDragStart = {
            startedAlpha = calculateAlpha(it.get(), size.max())
        },
        onDrag = { change, dragAmount ->
            change.consumeAllChanges()
            val actualAlpha = if (startedAlpha.isNaN()) {
                state.alpha
            } else {
                startedAlpha.also {
                    startedAlpha = Float.NaN
                }
            }
            val newAlpha = (actualAlpha + calculateAlpha(dragAmount.get(), size.max())).coerceIn(0.0f, 1.0f)
            state.update(alpha = newAlpha)
        }
    )
}

private fun DrawScope.drawTransparentBackground() {
    val squareSide = SQUARE_SIDE.toPx()
    val squareSize = Size(squareSide, squareSide)
    val stepsX = (size.width / squareSide).roundToInt() + 1
    val stepsY = (size.height / squareSide).roundToInt() + 1
    clipRect {
        repeat(stepsX) { x ->
            repeat(stepsY) { y ->
                val isRowEven = y % 2 == 0
                val color = when (x % 2 == 0) {
                    true -> when (isRowEven) {
                        true -> Color.Gray
                        false -> Color.LightGray
                    }
                    false -> when (isRowEven) {
                        true -> Color.LightGray
                        false -> Color.Gray
                    }
                }
                drawRect(color, Offset(x * squareSide, y * squareSide), squareSize)
            }
        }
    }
}

private fun calculateAlpha(
    x: Float,
    max: Int,
): Float {
    return x / max
}