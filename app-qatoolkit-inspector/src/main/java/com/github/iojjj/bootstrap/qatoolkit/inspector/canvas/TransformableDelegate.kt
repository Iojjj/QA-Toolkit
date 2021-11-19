package com.github.iojjj.bootstrap.qatoolkit.inspector.canvas

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toOffset
import kotlin.math.roundToInt

internal class TransformableDelegate(
    viewportState: State<Rect>,
    canvasSizeState: State<IntSize>,
) : Transformable {

    private val viewport by viewportState
    private val canvasSize by canvasSizeState

    override fun toDrawOffset(
        offset: Offset,
    ): Offset {
        val asViewportX = AXIS_X_MIN + offset.x / canvasSize.width * AXIS_X_LENGTH
        val asViewportY = AXIS_Y_MIN + offset.y / canvasSize.height * AXIS_Y_LENGTH
        return Offset(
            x = canvasSize.width * (asViewportX - viewport.left) / viewport.width,
            y = canvasSize.height * (asViewportY - viewport.top) / viewport.height,
        )
    }

    override fun toDrawOffset(offset: IntOffset): IntOffset {
        val viewportOffset = toDrawOffset(offset.toOffset())
        return IntOffset(viewportOffset.x.roundToInt(), viewportOffset.y.roundToInt())
    }

    override fun toDrawRect(rect: Rect): Rect {
        val newTopLeft = toDrawOffset(rect.topLeft)
        val newBottomRight = toDrawOffset(rect.topLeft + Offset(rect.size.width, rect.size.height))
        return Rect(newTopLeft, newBottomRight)
    }

    override fun toDrawRect(rect: IntRect): IntRect {
        val newTopLeft = toDrawOffset(rect.topLeft.toOffset())
        val newBottomRight = toDrawOffset(rect.topLeft.toOffset() + IntOffset(rect.size.width, rect.size.height).toOffset())
        return IntRect(
            topLeft = IntOffset(newTopLeft.x.roundToInt(), newTopLeft.y.roundToInt()),
            bottomRight = IntOffset(newBottomRight.x.roundToInt(), newBottomRight.y.roundToInt())
        )
    }

    override fun toDrawRect(roundRect: RoundRect): RoundRect {
        val newTopLeft = toDrawOffset(Offset(roundRect.left, roundRect.top))
        val newBottomRight = toDrawOffset(Offset(roundRect.right, roundRect.bottom))
        return RoundRect(
            newTopLeft.x,
            newTopLeft.y,
            newBottomRight.x,
            newBottomRight.y,
            roundRect.topLeftCornerRadius,
            roundRect.topRightCornerRadius,
            roundRect.bottomRightCornerRadius,
            roundRect.bottomLeftCornerRadius
        )
    }

    override fun fromDrawOffset(
        offset: Offset,
    ): Offset {
        val viewportOffset = toViewportOffset(offset)
        return fromViewportOffset(viewportOffset)
    }

    fun toViewportOffset(offset: Offset): Offset {
        return viewport.topLeft + toViewportDelta(offset)
    }

    fun fromViewportOffset(offset: Offset): Offset {
        return Offset(
            (offset.x - AXIS_X_MIN) * canvasSize.width / AXIS_X_LENGTH,
            (offset.y - AXIS_Y_MIN) * canvasSize.height / AXIS_Y_LENGTH,
        )
    }

    fun toViewportDelta(offset: Offset): Offset {
        return Offset(
            viewport.width * offset.x / canvasSize.width,
            viewport.height * offset.y / canvasSize.height,
        )
    }
}