package com.github.iojjj.bootstrap.qatoolkit.ruler

import android.text.TextPaint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.iojjj.bootstrap.pub.core.stdlib.INT_ALPHA_RANGE
import com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope.drawTextAbove
import com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope.drawTextAboveAndBelow
import com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope.drawTextBelow
import com.github.iojjj.bootstrap.qatoolkit.core.DimensionType
import kotlin.math.roundToInt

@Stable
internal class RulerTextDrawer(
    textPaintState: State<TextPaint>,
    private val layoutDirectionOffset: Animatable<Float, AnimationVector1D>,
    unpinnedPositionState: State<Float>,
    numberFormatterState: State<(Float) -> String>,
    percentFormatterState: State<(Float) -> String>,
    screenSizeState: State<IntSize>,
    isPercentVisibleState: State<Boolean>,
    textScaleState: State<Float>,
    dimensionTypeState: State<DimensionType>,
) {

    private val textPaint by textPaintState
    private val unpinnedPosition by unpinnedPositionState
    private val numberFormatter by numberFormatterState
    private val percentFormatter by percentFormatterState
    private val screenSize by screenSizeState
    private val isPercentVisible by isPercentVisibleState
    private val textScale by textScaleState
    private val dimensionType by dimensionTypeState

    private val stringBuilder = StringBuilder()

    @Stable
    fun draw(
        drawScope: DrawScope,
        points: List<Offset>
    ): Unit = with(drawScope) {
        val lastIndex = points.size / 2 - 1
        val textPositionOffset = layoutDirectionOffset.value / OFFSET_RANGE
        val textInsets = TEXT_INSETS.toPx() * textScale
        val spaceForText = size.width - 2 * textInsets

        val maxY = screenSize.height.toFloat()

        var abovePosition = 0.0f
        var belowPosition = size.height
        var firstAfterPosition = true
        var lastPosition = 0.0f

        // Draw pinned rulers.

        val originalPaintAlpha = textPaint.alpha
        textPaint.apply {
            textSize = PINNED_TEXT_SIZE.toPx() * textScale
            alpha = (originalPaintAlpha * PINNED_TEXT_ALPHA).roundToInt().coerceIn(INT_ALPHA_RANGE)
        }

        points.asSequence()
            .filterIndexed { index, _ -> index % 2 == 0 }
            .forEachIndexed { index, current ->
                if (current.y <= unpinnedPosition) {
                    val y = if (index == 0) {
                        current.y
                    } else {
                        current.y - points[2 * (index - 1)].y
                    }
                    val text = makeNewString("↑", textPositionOffset < 0.5, y, maxY)
                    val offset = Offset(
                        x = calculateTextOffsetX(textInsets, spaceForText, text, textPositionOffset),
                        y = calculateTextOffsetYAbove(textInsets, current.y)
                    )
                    drawTextAbove(text, offset, textPaint)
                    abovePosition = current.y
                } else {
                    if (firstAfterPosition) {
                        belowPosition = current.y
                        firstAfterPosition = false
                    }
                    val y = if (index < lastIndex) {
                        points[2 * (index + 1)].y - current.y
                    } else {
                        lastPosition = current.y
                        maxY - current.y
                    }
                    val text = makeNewString("↓", textPositionOffset < 0.5, y, maxY)
                    val offset = Offset(
                        x = calculateTextOffsetX(textInsets, spaceForText, text, textPositionOffset),
                        y = calculateTextOffsetYBelow(textInsets, current.y)
                    )
                    drawTextBelow(text, offset, textPaint)
                }
            }

        // Draw unpinned ruler.

        val yAbove = unpinnedPosition - abovePosition
        val yBelow = belowPosition - unpinnedPosition
        val textAbove = makeNewString("↑", textPositionOffset >= 0.5, yAbove, maxY)
        val textBelow = if (belowPosition == size.height && belowPosition != lastPosition && size.height < maxY) {
            // Show how much pixels to the bottom of screen
            makeNewString("↓", textPositionOffset >= 0.5, maxY - unpinnedPosition, maxY)
        } else {
            makeNewString("↓", textPositionOffset >= 0.5, yBelow, maxY)
        }
        val textSingleLine = "$textAbove    $textBelow"

        textPaint.apply {
            textSize = UNPINNED_TEXT_SIZE.toPx() * textScale
            alpha = (originalPaintAlpha * UNPINNED_TEXT_ALPHA).roundToInt().coerceIn(INT_ALPHA_RANGE)
        }

        val offsetAbove = Offset(
            x = calculateTextOffsetX(textInsets, spaceForText, textAbove, 1 - textPositionOffset),
            y = calculateTextOffsetYAbove(textInsets, unpinnedPosition)
        )
        val offsetBelow = Offset(
            x = calculateTextOffsetX(textInsets, spaceForText, textBelow, 1 - textPositionOffset),
            y = calculateTextOffsetYBelow(textInsets, unpinnedPosition)
        )
        val offsetSingleLineAbove = Offset(
            x = calculateTextOffsetX(textInsets, spaceForText, textSingleLine, 1 - textPositionOffset),
            y = calculateTextOffsetYAbove(textInsets, unpinnedPosition)
        )
        val offsetSingleLineBelow = Offset(
            x = calculateTextOffsetX(textInsets, spaceForText, textSingleLine, 1 - textPositionOffset),
            y = calculateTextOffsetYBelow(textInsets, unpinnedPosition)
        )

        drawTextAboveAndBelow(
            textAbove = textAbove,
            textBelow = textBelow,
            textSingleLine = textSingleLine,
            offsetAbove = offsetAbove,
            offsetBelow = offsetBelow,
            offsetSingleLineAbove = offsetSingleLineAbove,
            offsetSingleLineBelow = offsetSingleLineBelow,
            textPaint = textPaint,
            minY = abovePosition,
            maxY = belowPosition
        )
        textPaint.alpha = originalPaintAlpha
    }

    private fun calculateTextOffsetX(
        textInsets: Float,
        spaceForText: Float,
        text: String,
        textPositionOffset: Float
    ): Float {
        val textWidth = textPaint.measureText(text)
        return textInsets + textWidth / 2 + (spaceForText - textWidth) * textPositionOffset
    }

    private fun calculateTextOffsetYAbove(
        textInsets: Float,
        y: Float
    ): Float {
        return y - textInsets
    }

    private fun calculateTextOffsetYBelow(
        textInsets: Float,
        y: Float
    ): Float {
        return y + textInsets
    }

    private fun Density.makeNewString(
        symbol: String,
        prepend: Boolean,
        y: Float,
        maxY: Float,
    ): String = with(stringBuilder) {
        clear()
        wrappedWithSymbol(symbol, prepend) {
            val value = when (dimensionType) {
                DimensionType.DP -> y / density
                DimensionType.PX -> y
            }
            append(numberFormatter(value))
            if (isPercentVisible) {
                append(" (")
                append(percentFormatter(y / maxY))
                append(")")
            }
        }
        toString()
    }

    private inline fun StringBuilder.wrappedWithSymbol(
        symbol: String,
        prepend: Boolean,
        block: StringBuilder.() -> Unit,
    ) {
        if (prepend) {
            append(symbol)
            append(" ")
        }
        block()
        if (!prepend) {
            append(" ")
            append(symbol)
        }
    }

    companion object {
        private val UNPINNED_TEXT_SIZE = 14.sp
        private val PINNED_TEXT_SIZE = 12.sp
        private const val UNPINNED_TEXT_ALPHA = 1.0f
        private const val PINNED_TEXT_ALPHA = 0.75f
        private val TEXT_INSETS = 4.dp
    }
}

@Composable
internal fun rememberRulerTextDrawer(
    textColor: Color,
    numberFormatter: (Float) -> String,
    percentFormatter: (Float) -> String,
    isPercentVisible: Boolean,
    textScale: Float,
    dimensionType: DimensionType,
    layoutDirectionState: Animatable<Float, AnimationVector1D>,
    unpinnedPositionState: State<Float>,
    screenSizeState: State<IntSize>,
): RulerTextDrawer {
    val textPaintState = rememberUpdatedTextPaintState(textColor)
    val numberFormatterState = rememberUpdatedState(numberFormatter)
    val percentFormatterState = rememberUpdatedState(percentFormatter)
    val isPercentVisibleState = rememberUpdatedState(isPercentVisible)
    val textScaleState = rememberUpdatedState(textScale)
    val dimensionTypeState = rememberUpdatedState(dimensionType)
    val rulerTextDrawer = remember {
        RulerTextDrawer(
            textPaintState,
            layoutDirectionState,
            unpinnedPositionState,
            numberFormatterState,
            percentFormatterState,
            screenSizeState,
            isPercentVisibleState,
            textScaleState,
            dimensionTypeState
        )
    }
    return rulerTextDrawer
}

@Composable
internal fun rememberUpdatedTextPaintState(textColor: Color): State<TextPaint> {
    val textPaint = remember {
        mutableStateOf(
            TextPaint(TextPaint.ANTI_ALIAS_FLAG).apply {
                setShadowLayer(5f, 2f, 2f, Color.Black.toArgb())
            },
            policy = neverEqualPolicy()
        )
    }
    val argbColor = textColor.toArgb()
    if (textPaint.value.color != argbColor) {
        textPaint.value = textPaint.value.apply {
            color = argbColor
        }
    }
    return textPaint
}