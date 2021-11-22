package com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.github.iojjj.bootstrap.pub.core.extensions.toIntAlpha
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ColorAttribute
import java.text.DecimalFormat

private val FLOAT_FORMATTER = DecimalFormat("0.0#")

@Composable
internal fun AttributeColor(
    name: AnnotatedString,
    attribute: ColorAttribute,
) {
    val colorTypeState = remember {
        mutableStateOf(ColorType.HEX)
    }
    val color = Color(attribute.color)
    AttributeTemplate(
        name = name,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) {
                colorTypeState.value = when (colorTypeState.value) {
                    ColorType.HEX -> ColorType.ARGB_INT
                    ColorType.ARGB_INT -> ColorType.ARGB_FLOAT
                    ColorType.ARGB_FLOAT -> ColorType.HEX
                }
            }
    ) {
        Row {
            val painter = remember {
                ColorPainter(color)
            }
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .previewBackground()
                    .align(Alignment.CenterVertically)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = when (colorTypeState.value) {
                    ColorType.HEX -> {
                        "#${Integer.toHexString(color.toArgb())}"
                    }
                    ColorType.ARGB_INT -> {
                        "a = ${color.alpha.toIntAlpha()}, " +
                            "r = ${color.red.toIntAlpha()}, " +
                            "g = ${color.green.toIntAlpha()}, " +
                            "b = ${color.blue.toIntAlpha()}"
                    }
                    ColorType.ARGB_FLOAT -> {
                        "a = ${FLOAT_FORMATTER.format(color.alpha)}, " +
                            "r = ${FLOAT_FORMATTER.format(color.red)}, " +
                            "g = ${FLOAT_FORMATTER.format(color.green)}, " +
                            "b = ${FLOAT_FORMATTER.format(color.blue)}"
                    }
                },
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}