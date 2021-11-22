package com.github.iojjj.bootstrap.qatoolkit.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScaleSlider(
    minScale: Float = 0.5f,
    maxScale: Float = 5.0f,
    mode: SliderMode = SliderMode.Discrete(0.1f),
    scale: Float,
    onScaleChange: (scale: Float) -> Unit,
) {
    val labelFormatterState = rememberScaleFormatter(mode)
    CommonSlider(
        mode = mode,
        valueFrom = minScale,
        valueTo = maxScale,
        value = scale,
        onValueChange = onScaleChange,
        labelFormatter = labelFormatterState
    )
}

@Composable
fun rememberScaleFormatter(mode: SliderMode = SliderMode.Continuous): (Float) -> String {
    return remember(mode) {
        makeScaleFormatter(mode)
    }
}

@Stable
private fun makeScaleFormatter(
    mode: SliderMode,
): (Float) -> String {
    val delegate = makeDecimalFormatter(mode)
    return {
        "x${delegate(it)}"
    }
}

@Preview
@Composable
private fun PreviewScaleSlider() {
    val (scale, onScaleChange) = remember { mutableStateOf(1.0f) }
    ScaleSlider(
        scale = scale,
        onScaleChange = onScaleChange,
        minScale = 0.5f,
        maxScale = 5.0f,
        mode = SliderMode.Discrete(0.25f),
    )
}