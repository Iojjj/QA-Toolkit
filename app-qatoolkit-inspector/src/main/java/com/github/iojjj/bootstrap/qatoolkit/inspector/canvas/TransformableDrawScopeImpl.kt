package com.github.iojjj.bootstrap.qatoolkit.inspector.canvas

import androidx.compose.runtime.State
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawContext
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

internal class TransformableDrawScopeImpl(
    private val state: TransformableCanvasState,
) : TransformableDrawScope,
    DrawScope,
    Transformable {

    private val pointsMapper: (Offset) -> Offset = ::toDrawOffset
    private var isRotated = false
    private val transformableDelegate = TransformableDelegate(
        object : State<Rect> {
            override val value: Rect
                get() {
                    val origin = state.viewportState.value
                    return if (isRotated) {
                        Rect(
                            left = -origin.bottom,
                            top = origin.left,
                            right = -origin.top,
                            bottom = origin.right
                        )
                    } else {
                        origin
                    }
                }
        },
        object : State<IntSize> {
            override val value: IntSize
                get() = IntSize(
                    size.width.roundToInt(),
                    size.height.roundToInt(),
                )
        }
    )

    lateinit var delegate: DrawScope

    override val drawContext: DrawContext
        get() = delegate.drawContext
    override val layoutDirection: LayoutDirection
        get() = delegate.layoutDirection
    override val density: Float
        get() = delegate.density
    override val fontScale: Float
        get() = delegate.fontScale

    override val size: Size
        get() {
            val origin = super<TransformableDrawScope>.size
            return if (isRotated) {
                Size(origin.height, origin.width)
            } else {
                origin
            }
        }

    override val viewportSize: Size
        get() = Size(
            width = size.width * (state.viewportState.value.right - state.viewportState.value.left) / AXIS_X_LENGTH,
            height = size.height * (state.viewportState.value.bottom - state.viewportState.value.top) / AXIS_Y_LENGTH,
        )
    override val viewportZoom: Float
        get() = state.zoom

    override val sharedTransformablePath: TransformablePath = TransformablePathImpl(Path(), transformableDelegate)

    override fun drawArc(
        brush: Brush,
        startAngle: Float,
        sweepAngle: Float,
        useCenter: Boolean,
        topLeft: Offset,
        size: Size,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newTopLeft = toDrawOffset(topLeft)
        val newSize = adjustRectSize(topLeft, newTopLeft, size)
        delegate.drawArc(brush, startAngle, sweepAngle, useCenter, newTopLeft, newSize, alpha, style, colorFilter, blendMode)
    }

    override fun drawArc(
        color: Color,
        startAngle: Float,
        sweepAngle: Float,
        useCenter: Boolean,
        topLeft: Offset,
        size: Size,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newTopLeft = toDrawOffset(topLeft)
        val newSize = adjustRectSize(topLeft, newTopLeft, size)
        delegate.drawArc(color, startAngle, sweepAngle, useCenter, newTopLeft, newSize, alpha, style, colorFilter, blendMode)
    }

    override fun drawCircle(
        brush: Brush,
        radius: Float,
        center: Offset,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newCenter = toDrawOffset(center)
        delegate.drawCircle(brush, radius, newCenter, alpha, style, colorFilter, blendMode)
    }

    override fun drawCircle(
        color: Color,
        radius: Float,
        center: Offset,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newCenter = toDrawOffset(center)
        delegate.drawCircle(color, radius, newCenter, alpha, style, colorFilter, blendMode)
    }

    override fun drawImage(
        image: ImageBitmap,
        topLeft: Offset,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        TODO("Not yet implemented")
    }

    override fun drawImage(
        image: ImageBitmap,
        srcOffset: IntOffset,
        srcSize: IntSize,
        dstOffset: IntOffset,
        dstSize: IntSize,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        TODO("Not yet implemented")
    }

    override fun drawLine(
        brush: Brush,
        start: Offset,
        end: Offset,
        strokeWidth: Float,
        cap: StrokeCap,
        pathEffect: PathEffect?,
        alpha: Float,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newStart = toDrawOffset(start)
        val newEnd = toDrawOffset(end)
        delegate.drawLine(brush, newStart, newEnd, strokeWidth, cap, pathEffect, alpha, colorFilter, blendMode)
    }

    override fun drawLine(
        color: Color,
        start: Offset,
        end: Offset,
        strokeWidth: Float,
        cap: StrokeCap,
        pathEffect: PathEffect?,
        alpha: Float,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newStart = toDrawOffset(start)
        val newEnd = toDrawOffset(end)
        delegate.drawLine(color, newStart, newEnd, strokeWidth, cap, pathEffect, alpha, colorFilter, blendMode)
    }

    override fun drawOval(
        brush: Brush,
        topLeft: Offset,
        size: Size,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newTopLeft = toDrawOffset(topLeft)
        val newSize = adjustRectSize(topLeft, newTopLeft, size)
        delegate.drawOval(brush, newTopLeft, newSize, alpha, style, colorFilter, blendMode)
    }

    override fun drawOval(
        color: Color,
        topLeft: Offset,
        size: Size,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newTopLeft = toDrawOffset(topLeft)
        val newSize = adjustRectSize(topLeft, newTopLeft, size)
        delegate.drawOval(color, newTopLeft, newSize, alpha, style, colorFilter, blendMode)
    }

    override fun drawPath(
        path: TransformablePath,
        color: Color,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        delegate.drawPath(path.delegate, color, alpha, style, colorFilter, blendMode)
    }

    override fun drawPath(
        path: TransformablePath,
        brush: Brush,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        delegate.drawPath(path.delegate, brush, alpha, style, colorFilter, blendMode)
    }

    override fun drawPath(
        path: Path,
        brush: Brush,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val actualPath = (path as? TransformablePath)
            ?.delegate
            ?: path
        delegate.drawPath(actualPath, brush, alpha, style, colorFilter, blendMode)
    }

    override fun drawPath(
        path: Path,
        color: Color,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val actualPath = (path as? TransformablePath)
            ?.delegate
            ?: path
        delegate.drawPath(actualPath, color, alpha, style, colorFilter, blendMode)
    }

    override fun drawPoints(
        points: List<Offset>,
        pointMode: PointMode,
        brush: Brush,
        strokeWidth: Float,
        cap: StrokeCap,
        pathEffect: PathEffect?,
        alpha: Float,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        delegate.drawPoints(points.map(pointsMapper), pointMode, brush, strokeWidth, cap, pathEffect, alpha, colorFilter, blendMode)
    }

    override fun drawPoints(
        points: List<Offset>,
        pointMode: PointMode,
        color: Color,
        strokeWidth: Float,
        cap: StrokeCap,
        pathEffect: PathEffect?,
        alpha: Float,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        delegate.drawPoints(points.map(pointsMapper), pointMode, color, strokeWidth, cap, pathEffect, alpha, colorFilter, blendMode)
    }

    override fun drawRect(
        brush: Brush,
        topLeft: Offset,
        size: Size,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newTopLeft = toDrawOffset(topLeft)
        val newSize = adjustRectSize(topLeft, newTopLeft, size)
        delegate.drawRect(brush, newTopLeft, newSize, alpha, style, colorFilter, blendMode)
    }

    override fun drawRect(
        color: Color,
        topLeft: Offset,
        size: Size,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newTopLeft = toDrawOffset(topLeft)
        val newSize = adjustRectSize(topLeft, newTopLeft, size)
        delegate.drawRect(color, newTopLeft, newSize, alpha, style, colorFilter, blendMode)
    }

    override fun drawRoundRect(
        brush: Brush,
        topLeft: Offset,
        size: Size,
        cornerRadius: CornerRadius,
        alpha: Float,
        style: DrawStyle,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newTopLeft = toDrawOffset(topLeft)
        val newSize = adjustRectSize(topLeft, newTopLeft, size)
        delegate.drawRoundRect(brush, newTopLeft, newSize, cornerRadius, alpha, style, colorFilter, blendMode)
    }

    override fun drawRoundRect(
        color: Color,
        topLeft: Offset,
        size: Size,
        cornerRadius: CornerRadius,
        style: DrawStyle,
        alpha: Float,
        colorFilter: ColorFilter?,
        blendMode: BlendMode,
    ) {
        val newTopLeft = toDrawOffset(topLeft)
        val newSize = adjustRectSize(topLeft, newTopLeft, size)
        delegate.drawRoundRect(color, newTopLeft, newSize, cornerRadius, style, alpha, colorFilter, blendMode)
    }

    override fun newTransformablePath(): TransformablePath {
        return TransformablePathImpl(Path(), transformableDelegate)
    }

    override fun toDrawOffset(offset: Offset): Offset {
        return transformableDelegate.toDrawOffset(offset)
    }

    override fun toDrawOffset(offset: IntOffset): IntOffset {
        return transformableDelegate.toDrawOffset(offset)
    }

    override fun toDrawRect(rect: Rect): Rect {
        return transformableDelegate.toDrawRect(rect)
    }

    override fun toDrawRect(rect: IntRect): IntRect {
        return transformableDelegate.toDrawRect(rect)
    }

    override fun toDrawRect(roundRect: RoundRect): RoundRect {
        return transformableDelegate.toDrawRect(roundRect)
    }

    override fun fromDrawOffset(offset: Offset): Offset {
        return transformableDelegate.fromDrawOffset(offset)
    }

    override fun vertically(onDraw: TransformableDrawScope.() -> Unit) {
        rotate(-90f, pivot = Offset.Zero) {
            translate(left = -size.height) {
                with(this@TransformableDrawScopeImpl) {
                    isRotated = true
                    onDraw()
                    isRotated = false
                }
            }
        }
    }

    private fun adjustRectSize(
        topLeft: Offset,
        adjustedTopLeft: Offset,
        size: Size,
    ): Size {
        val newBottomRight = toDrawOffset(topLeft + Offset(size.width, size.height))
        return Size(newBottomRight.x - adjustedTopLeft.x, newBottomRight.y - adjustedTopLeft.y)
    }
}