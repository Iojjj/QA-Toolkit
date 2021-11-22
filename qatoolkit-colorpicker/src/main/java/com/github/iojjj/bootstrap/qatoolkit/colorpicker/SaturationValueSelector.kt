package com.github.iojjj.bootstrap.qatoolkit.colorpicker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.iojjj.bootstrap.qatoolkit.colorpicker.style.ColorPickerStyle

@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun SaturationValuePicker(
    state: ColorPickerState,
    modifier: Modifier,
    indicatorRadius: Dp = 5.dp,
    indicatorColor: Color = ColorPickerStyle.Colors.colorIndicator,
    indicatorStrokeColor: Color = ColorPickerStyle.Colors.colorIndicatorStroke,
    indicatorStrokeWidth: Dp = 1.dp,
) {
    val saturationBrush = remember(state.hue) {
        horizontalGradient(listOf(Color.White, Color.hsv(state.hue, 1.0f, 1.0f)))
    }
    val valueBrush = remember {
        verticalGradient(listOf(Color.Transparent, Color.Black))
    }
    val density = LocalDensity.current
    val indicatorStrokeStyle = remember(density, indicatorStrokeWidth) {
        with(density) {
            when {
                indicatorStrokeWidth.value > 0.0f -> Stroke(width = indicatorStrokeWidth.toPx())
                else -> null
            }
        }
    }
    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    val newSaturation = calculateSaturation(it.x)
                    val newValue = calculateValue(it.y)
                    state.update(saturation = newSaturation, value = newValue)
                }
            }
            .pointerInput(Unit) {
                var startedSaturation = Float.NaN
                var startedValue = Float.NaN
                detectDragGestures(
                    onDragStart = {
                        startedSaturation = calculateSaturation(it.x)
                        startedValue = calculateValue(it.y)
                    },
                    onDrag = { change, dragAmount ->
                        change.consumeAllChanges()
                        val actualSaturation = if (startedSaturation.isNaN()) {
                            state.saturation
                        } else {
                            startedSaturation.also {
                                startedSaturation = Float.NaN
                            }
                        }
                        val actualValue = if (startedValue.isNaN()) {
                            state.value
                        } else {
                            startedValue.also {
                                startedValue = Float.NaN
                            }
                        }
                        val newSaturation = (actualSaturation + calculateSaturation(dragAmount.x)).coerceIn(0.0f, 1.0f)
                        val newValue = (calculateValue(dragAmount.y) + actualValue - 1.0f).coerceIn(0.0f, 1.0f)
                        state.update(saturation = newSaturation, value = newValue)
                    }
                )
            },
        onDraw = {
            drawRect(saturationBrush)
            drawRect(valueBrush)
            val center = Offset(size.width * state.saturation, (1.0f - state.value) * size.height)
            drawCircle(indicatorColor, indicatorRadius.toPx(), center)
            if (indicatorStrokeStyle != null) {
                drawCircle(indicatorStrokeColor, indicatorRadius.toPx(), center, style = indicatorStrokeStyle)
            }
        }
    )
}

private fun PointerInputScope.calculateSaturation(x: Float): Float {
    return x / size.width
}

private fun PointerInputScope.calculateValue(y: Float): Float {
    return 1.0f - y / size.height
}