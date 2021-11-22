package com.github.iojjj.bootstrap.qatoolkit.settings

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.roundToInt

@Composable
fun CommonSlider(
    mode: SliderMode,
    valueFrom: Float,
    valueTo: Float,
    value: Float,
    onValueChange: (value: Float) -> Unit,
    labelFormatter: ((value: Float) -> String)? = null,
) {
    val valueFromState = rememberUpdatedState(valueFrom)
    val onValueChangeState = rememberUpdatedState(onValueChange)
    val steps = remember(mode, valueFrom, valueTo) {
        when (mode) {
            is SliderMode.Discrete -> mode.numberOfSteps(valueFrom, valueTo)
            SliderMode.Continuous -> 0
        }
    }
    val valueRange = remember(valueFrom, valueTo) {
        0.0f..(valueTo - valueFrom)
    }
    val labelFormatterState = remember(labelFormatter, mode) {
        labelFormatter ?: makeDecimalFormatter(mode)
    }
    val localOnValueChangeState = remember<(Float) -> Unit> {
        {
            onValueChangeState.value.invoke(valueFromState.value + it)
        }
    }
    Column {
        Slider(
            value = value - valueFrom,
            valueRange = valueRange,
            steps = steps,
            onValueChange = localOnValueChangeState,
        )
        Text(
            text = labelFormatterState(value),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Stable
sealed interface SliderMode {

    object Continuous : SliderMode

    data class Discrete(
        @FloatRange(
            from = 0.0,
            fromInclusive = false
        )
        val step: Float,
    ) : SliderMode {

        init {
            require(step > 0) {
                "Step must be greater than zero."
            }
        }

        fun numberOfSteps(
            minScale: Float,
            maxScale: Float,
        ): Int {
            val steps = (maxScale - minScale) / step
            return steps.roundToInt() - 1
        }
    }
}

@Stable
internal fun makeDecimalFormatter(
    mode: SliderMode,
): (Float) -> String {
    return when (mode) {
        is SliderMode.Discrete -> {
            makeDiscreteLabelFormatter(mode.step)
        }
        SliderMode.Continuous -> {
            makeSimpleFormatter(decimals = 2)
        }
    }
}

@Stable
internal fun makeSimpleFormatter(decimals: Int): (Float) -> String {
    val formatter = createNumberFormat(decimals)
    return {
        formatter.format(it)
    }
}

@Stable
private fun makeDiscreteLabelFormatter(
    step: Float,
    decimals: Int = calculateDecimals(step),
): (Float) -> String {
    val formatter = createNumberFormat(decimals)
    return {
        val rem = it.rem(step)
        if (rem < step / 2) {
            formatter.format(it - rem)
        } else {
            formatter.format(it + step - rem)
        }
    }
}

@Stable
private fun calculateDecimals(step: Float): Int {
    return step.toString()
        .split(".")
        .last()
        .length
}

@Stable
private fun createNumberFormat(decimals: Int): NumberFormat {
    return if (decimals <= 0) {
        NumberFormat.getIntegerInstance()
    } else {
        val zeros = "".padEnd(decimals, '0')
        return DecimalFormat("0.$zeros")
    }
}