package com.github.iojjj.bootstrap.qatoolkit.colorpicker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.style.ColorPickerStyle

private val HUE_COLORS = listOf(
    Color(red = 1.0f, green = 0.0f, blue = 0.0f),
    Color(red = 1.0f, green = 1.0f, blue = 0.0f),
    Color(red = 0.0f, green = 1.0f, blue = 0.0f),
    Color(red = 0.0f, green = 1.0f, blue = 1.0f),
    Color(red = 0.0f, green = 0.0f, blue = 1.0f),
    Color(red = 1.0f, green = 0.0f, blue = 1.0f),
    Color(red = 1.0f, green = 0.0f, blue = 0.0f),
)

private val TRIANGLE_SIDE = 6.dp
private const val SQRT_3 = 1.732f // ~sqrt(3)

@Composable
fun HorizontalHuePicker(
    state: ColorPickerState,
    modifier: Modifier,
    indicatorColor: Color = ColorPickerStyle.Colors.hueIndicator,
) {
    val onTapChangeHue = remember {
        createOnTapChangeHue(state, Offset::x, IntSize::width)
    }
    val onDragChangeHue = remember {
        createOnDragChangeHue(state, Offset::x, IntSize::width)
    }
    Canvas(
        modifier = modifier
            .pointerInput(Unit, onTapChangeHue)
            .pointerInput(Unit, onDragChangeHue),
        onDraw = rememberHorizontalHueDrawer(state, indicatorColor)
    )
}

@Composable
fun VerticalHuePicker(
    state: ColorPickerState,
    modifier: Modifier,
    indicatorColor: Color = ColorPickerStyle.Colors.hueIndicator,
) {
    val onTapChangeHue = remember {
        createOnTapChangeHue(state, Offset::y, IntSize::height)
    }
    val onDragChangeHue = remember {
        createOnDragChangeHue(state, Offset::y, IntSize::height)
    }
    Canvas(
        modifier = modifier
            .pointerInput(Unit, onTapChangeHue)
            .pointerInput(Unit, onDragChangeHue),
        onDraw = rememberVerticalHueDrawer(state, indicatorColor)
    )
}

@Composable
private fun rememberHorizontalHueDrawer(
    state: ColorPickerState,
    indicatorColor: Color,
): DrawScope.() -> Unit {
    val hueBrush = remember {
        horizontalGradient(HUE_COLORS)
    }
    val path = remember {
        Path()
    }
    val indicatorColorState = rememberUpdatedState(indicatorColor)
    return remember {
        {
            drawRect(hueBrush)
            val x = state.hue * size.width / 360
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

@Composable
private fun rememberVerticalHueDrawer(
    state: ColorPickerState,
    indicatorColor: Color,
): DrawScope.() -> Unit {
    val hueBrush = remember {
        verticalGradient(HUE_COLORS)
    }
    val path = remember {
        Path()
    }
    val indicatorColorState = rememberUpdatedState(indicatorColor)
    return remember {
        {
            drawRect(hueBrush)
            val y = state.hue * size.height / 360
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

private fun createOnTapChangeHue(
    state: ColorPickerState,
    get: Offset.() -> Float,
    max: IntSize.() -> Int,
): suspend PointerInputScope.() -> Unit = {
    detectTapGestures {
        state.update(hue = calculateHue(it.get(), size.max()))
    }
}

private fun createOnDragChangeHue(
    state: ColorPickerState,
    get: Offset.() -> Float,
    max: IntSize.() -> Int,
): suspend PointerInputScope.() -> Unit = {
    var startedHue = Float.NaN
    detectDragGestures(
        onDragStart = {
            startedHue = calculateHue(it.get(), size.max())
        },
        onDrag = { change, dragAmount ->
            change.consumeAllChanges()
            val actualHue = if (startedHue.isNaN()) {
                state.hue
            } else {
                startedHue.also {
                    startedHue = Float.NaN
                }
            }
            val newHue = (actualHue + calculateHue(dragAmount.get(), size.max())).coerceIn(0.0f, 360.0f)
            state.update(hue = newHue)
        }
    )
}

private fun calculateHue(
    x: Float,
    max: Int,
): Float {
    return 360.0f * x / max
}