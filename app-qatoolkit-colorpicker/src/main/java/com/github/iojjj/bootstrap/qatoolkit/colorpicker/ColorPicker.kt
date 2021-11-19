package com.github.iojjj.bootstrap.qatoolkit.colorpicker

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun ColorPicker(
    state: ColorPickerState,
    modifier: Modifier = Modifier,
    allowChangeAlpha: Boolean = true,
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .height(IntrinsicSize.Min)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier.border(1.dp, Color.Black.copy(alpha = 0.25f))
        ) {
            SaturationValuePicker(
                state = state,
                modifier = Modifier.size(120.dp).clipToBounds(),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier.border(1.dp, Color.Black.copy(alpha = 0.25f))
        ) {
            VerticalHuePicker(
                state = state,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(24.dp)
                    .clipToBounds()
            )
        }
        if (allowChangeAlpha) {
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier.border(1.dp, Color.Black.copy(alpha = 0.25f))
            ) {
                VerticalAlphaPicker(
                    state = state,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(24.dp)
                        .clipToBounds()
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewColorPicker() {
    ColorPicker(rememberColorPickerState(Color.Blue))
}