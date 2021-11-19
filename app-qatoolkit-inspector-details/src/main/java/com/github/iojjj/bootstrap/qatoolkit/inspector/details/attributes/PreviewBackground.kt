package com.github.iojjj.bootstrap.qatoolkit.inspector.details.attributes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect.Companion.dashPathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

private val STROKE_WIDTH = 1.dp
private val DASH_INTERVALS = 1.dp

@Composable
internal fun rememberPreviewStroke(): DrawStyle {
    val density = LocalDensity.current
    return remember(density) {
        with(density) {
            Stroke(
                width = STROKE_WIDTH.toPx(),
                pathEffect = dashPathEffect(floatArrayOf(DASH_INTERVALS.toPx(), DASH_INTERVALS.toPx()))
            )
        }
    }
}

@Composable
internal fun rememberDrawBehind(): DrawScope.() -> Unit {
    val previewStrokeState = rememberUpdatedState(rememberPreviewStroke())
    return remember {
        {
            drawRect(
                color = Color.Black.copy(alpha = 0.5f),
                style = previewStrokeState.value
            )
        }
    }
}

@Composable
internal fun Modifier.previewBackground(): Modifier {
    return composed {
        drawBehind(rememberDrawBehind())
    }
}