package com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.qatoolkit.compose.core.geometry.containsInclusive
import kotlin.math.abs

val Paint.extraAbove: Float
    get() = -(abs(fontMetrics.bottom) + fontMetrics.leading)

val Paint.extraBelow: Float
    get() = abs(fontMetrics.top)

val Paint.textHeight: Float
    get() = abs(fontMetrics.top) + abs(fontMetrics.bottom)

fun DrawScope.drawTextAbove(
    text: String,
    offset: Offset,
    textPaint: Paint,
) {
    drawText(text, offset.copy(y = offset.y + textPaint.extraAbove), textPaint)
}

fun DrawScope.drawTextBelow(
    text: String,
    offset: Offset,
    textPaint: Paint,
) {
    drawText(text, offset.copy(y = offset.y + textPaint.extraBelow), textPaint)
}

fun DrawScope.drawTextAboveAndBelow(
    textAbove: String,
    textBelow: String,
    textSingleLine: String,
    offsetAbove: Offset,
    offsetBelow: Offset,
    offsetSingleLineAbove: Offset = offsetAbove,
    offsetSingleLineBelow: Offset = offsetBelow,
    textPaint: Paint,
    minY: Float,
    maxY: Float,
) {
    if (tryDrawTextAboveAndBelow(textAbove, textBelow, offsetAbove, offsetBelow, textPaint, minY, maxY)) {
        return
    }
    if (tryDrawTextAbove(textSingleLine, offsetSingleLineAbove, textPaint, minY)) {
        return
    }
    if (tryDrawTextBelow(textSingleLine, offsetSingleLineBelow, textPaint, maxY)) {
        return
    }
    exhaustive..when {
        minY == 0.0f -> {
            // Top, so draw below
            drawTextBelow(textSingleLine, offsetSingleLineBelow, textPaint)
        }
        maxY == size.height -> {
            // Bottom, so draw above
            drawTextAbove(textSingleLine, offsetSingleLineAbove, textPaint)
        }
        else -> {
            // Nothing to do.
            drawTextAbove(textAbove, offsetAbove, textPaint)
            drawTextBelow(textBelow, offsetBelow, textPaint)
        }
    }
}

fun DrawScope.drawTextAboveOrBelow(
    text: String,
    offset: Offset,
    textPaint: Paint,
    minY: Float,
    maxY: Float,
) {
    if (!tryDrawTextAboveOrBelow(text, offset, textPaint, minY, maxY)) {
        exhaustive..when {
            minY == 0.0f -> {
                // Top, so draw below
                drawTextBelow(text, offset, textPaint)
            }
            maxY == size.height -> {
                // Bottom, so draw above
                drawTextAbove(text, offset, textPaint)
            }
            else -> {
                // Nothing to do.
                drawTextAbove(text, offset, textPaint)
            }
        }
    }
}

fun DrawScope.drawTextBelowOrAbove(
    text: String,
    offset: Offset,
    textPaint: Paint,
    minY: Float,
    maxY: Float,
) {
    if (!tryDrawTextBelowOrAbove(text, offset, textPaint, minY, maxY)) {
        exhaustive..when {
            maxY == size.height -> {
                // Bottom, so draw above
                drawTextAbove(text, offset, textPaint)
            }
            minY == 0.0f -> {
                // Top, so draw below
                drawTextBelow(text, offset, textPaint)
            }
            else -> {
                // Nothing to do.
                drawTextBelow(text, offset, textPaint)
            }
        }
    }
}

fun DrawScope.tryDrawTextAboveOrBelow(
    text: String,
    offset: Offset,
    textPaint: Paint,
    minY: Float,
    maxY: Float,
): Boolean {
    return when {
        tryDrawTextAbove(text, offset, textPaint, minY) -> {
            true
        }
        tryDrawTextBelow(text, offset, textPaint, maxY) -> {
            true
        }
        else -> {
            false
        }
    }
}

fun DrawScope.tryDrawTextBelowOrAbove(
    text: String,
    offset: Offset,
    textPaint: Paint,
    minY: Float,
    maxY: Float,
): Boolean {
    return when {
        tryDrawTextBelow(text, offset, textPaint, maxY) -> {
            true
        }
        tryDrawTextAbove(text, offset, textPaint, minY) -> {
            true
        }
        else -> {
            false
        }
    }
}

fun DrawScope.tryDrawTextAbove(
    text: String,
    offset: Offset,
    textPaint: Paint,
    minY: Float,
): Boolean {
    val textHeight = textPaint.textHeight
    val extraAbove = textPaint.extraAbove
    val hasEnoughSpaceAbove = (offset.y + extraAbove - textHeight) >= minY
    return if (hasEnoughSpaceAbove) {
        drawTextAbove(text, offset, textPaint)
        true
    } else {
        false
    }
}

fun DrawScope.tryDrawTextBelow(
    text: String,
    offset: Offset,
    textPaint: Paint,
    maxY: Float,
): Boolean {
    val textHeight = textPaint.textHeight
    val extraBelow = textPaint.extraBelow
    val hasEnoughSpaceBelow = (offset.y + extraBelow + textHeight) <= maxY
    return if (hasEnoughSpaceBelow) {
        drawTextBelow(text, offset, textPaint)
        true
    } else {
        false
    }
}

fun DrawScope.tryDrawTextAboveAndBelow(
    textAbove: String,
    textBelow: String,
    offsetAbove: Offset,
    offsetBelow: Offset,
    textPaint: Paint,
    minY: Float,
    maxY: Float,
): Boolean {
    val textHeight = textPaint.textHeight
    val extraAbove = textPaint.extraAbove
    val extraBelow = textPaint.extraBelow
    val hasEnoughSpaceAbove = (offsetAbove.y + extraAbove - textHeight) >= minY
    val hasEnoughSpaceBelow = (offsetBelow.y + extraBelow + textHeight) <= maxY
    return when {
        hasEnoughSpaceAbove && hasEnoughSpaceBelow -> {
            drawTextAbove(textAbove, offsetAbove, textPaint)
            drawTextBelow(textBelow, offsetBelow, textPaint)
            true
        }
        else -> {
            false
        }
    }
}

fun DrawScope.drawText(
    text: String,
    offset: Offset = Offset.Zero,
    textPaint: Paint,
) {
    drawIntoCanvas {
        it.nativeCanvas.drawText(text, offset.x - textPaint.measureText(text) / 2, offset.y, textPaint)
    }
}

fun DrawScope.drawTextInside(
    text: String,
    rect: IntRect,
    textPaint: Paint,
    offset: Offset = Offset.Zero,
) {
    drawTextInside(
        text = text,
        rect = Rect(rect.topLeft.toOffset(), rect.size.toSize()),
        textPaint = textPaint,
        offset = offset
    )
}

fun DrawScope.drawTextInside(
    text: String,
    rect: Rect,
    textPaint: Paint,
    offset: Offset = Offset.Zero,
) {
    val center = rect.center
    val width = textPaint.measureText(text)
    val height = textPaint.textHeight
    val textOffset = center + offset

    if (rect.isFits(textOffset, width, height)) {
        drawText(
            text = text,
            offset = textOffset + Offset(0.0f, height / 2),
            textPaint = textPaint,
        )
    }
}

private fun Rect.isFits(
    newTextOffset: Offset,
    newWidth: Float,
    newHeight: Float,
): Boolean {
    val topLeft = newTextOffset + Offset(-newWidth / 2, -newHeight / 2)
    val bottomRight = newTextOffset + Offset(newWidth / 2, newHeight / 2)
    return containsInclusive(topLeft) && containsInclusive(bottomRight)
}
