package com.github.iojjj.bootstrap.qatoolkit.colorpicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi

@Stable
class ColorPickerState(
    initialColor: Color,
    private val onColorChange: (Color) -> Unit = { /* no-op */ },
) {
    private val hueState = mutableStateOf(0.0f)
    private val saturationState = mutableStateOf(1.0f)
    private val valueState = mutableStateOf(1.0f)
    private val alphaState = mutableStateOf(1.0f)
    private val colorState = mutableStateOf(initialColor)

    val hue: Float
        @Stable
        get() = hueState.value
    val saturation: Float
        @Stable
        get() = saturationState.value
    val value: Float
        @Stable
        get() = valueState.value
    val alpha: Float
        @Stable
        get() = alphaState.value
    val color: Color
        @Stable
        get() = colorState.value

    init {
        setColor(initialColor)
    }

    fun setColor(color: Color) {
        val max = maxOf(color.red, color.green, color.blue)
        val min = minOf(color.red, color.green, color.blue)
        val hue = when (max) {
            0.0f -> 0.0f
            min -> 0.0f
            color.red -> 60 * (0 + (color.green - color.blue) / (max - min))
            color.green -> 60 * (2 + (color.blue - color.red) / (max - min))
            color.blue -> 60 * (4 + (color.red - color.green) / (max - min))
            else -> error("Impossible case")
        }
        val saturation = when (max) {
            0.0f -> 0.0f
            else -> (max - min) / max
        }

        @Suppress("UnnecessaryVariable")
        val value = max
        update(hue, saturation, value, color.alpha, notifyColorChange = false)
    }

    @OptIn(ExperimentalGraphicsApi::class)
    internal fun update(
        hue: Float = hueState.value,
        saturation: Float = saturationState.value,
        value: Float = valueState.value,
        alpha: Float = alphaState.value,
        notifyColorChange: Boolean = true,
    ) {
        hueState.value = hue
        saturationState.value = saturation
        valueState.value = value
        alphaState.value = alpha
        colorState.value = Color.hsv(hue, saturation, value, alpha)
        if (notifyColorChange) {
            onColorChange(colorState.value)
        }
    }
}

@Composable
fun rememberColorPickerState(
    initialColor: Color,
    onColorChange: (Color) -> Unit = { /* no-op */ },
): ColorPickerState {
    return remember {
        ColorPickerState(initialColor, onColorChange)
    }
}