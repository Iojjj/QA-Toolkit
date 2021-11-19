package com.github.iojjj.bootstrap.qatoolkit.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
fun NumberSlider(
    valueFrom: Int = 0,
    valueTo: Int = 10,
    value: Int,
    onValueChange: (value: Int) -> Unit,
    labelFormatter: (Float) -> String = remember {
        makeSimpleFormatter(decimals = 0)
    },
) {
    val valueState = rememberUpdatedState(value)
    val onValueChangeState = rememberUpdatedState(onValueChange)
    val localValueState = remember {
        mutableStateOf(value.toFloat())
    }
    val localOnValueChangeState = remember<(Float) -> Unit> {
        { newValue ->
            val new = newValue.roundToInt()
            val old = valueState.value
            if (new != old) {
                onValueChangeState.value.invoke(new)
            }
            localValueState.value = newValue
        }
    }
    val mode = remember {
        SliderMode.Discrete(1.0f)
    }
    CommonSlider(
        mode = mode,
        valueFrom = valueFrom.toFloat(),
        valueTo = valueTo.toFloat(),
        value = localValueState.value,
        onValueChange = localOnValueChangeState,
        labelFormatter = labelFormatter
    )
}

@Preview
@Composable
private fun PreviewNumberSlider() {
    val (value, updateValue) = remember { mutableStateOf(5) }
    NumberSlider(
        valueFrom = 2,
        valueTo = 10,
        value = value,
        onValueChange = updateValue,
    )
}