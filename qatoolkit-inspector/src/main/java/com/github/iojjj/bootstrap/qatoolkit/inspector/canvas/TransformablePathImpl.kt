package com.github.iojjj.bootstrap.qatoolkit.inspector.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation

internal class TransformablePathImpl(
    override val delegate: Path,
    private val transformable: Transformable,
) : TransformablePath,
    Path by delegate {

    override fun addArc(
        oval: Rect,
        startAngleDegrees: Float,
        sweepAngleDegrees: Float,
    ) {
        delegate.addArc(transformable.toDrawRect(oval), startAngleDegrees, sweepAngleDegrees)
    }

    override fun addArcRad(
        oval: Rect,
        startAngleRadians: Float,
        sweepAngleRadians: Float,
    ) {
        delegate.addArc(transformable.toDrawRect(oval), startAngleRadians, sweepAngleRadians)
    }

    override fun addOval(oval: Rect) {
        delegate.addOval(transformable.toDrawRect(oval))
    }

    override fun addRect(rect: Rect) {
        delegate.addOval(transformable.toDrawRect(rect))
    }

    override fun addRoundRect(roundRect: RoundRect) {
        delegate.addRoundRect(transformable.toDrawRect(roundRect))
    }

    override fun arcTo(
        rect: Rect,
        startAngleDegrees: Float,
        sweepAngleDegrees: Float,
        forceMoveTo: Boolean,
    ) {
        delegate.arcTo(transformable.toDrawRect(rect), startAngleDegrees, sweepAngleDegrees, forceMoveTo)
    }

    override fun cubicTo(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        x3: Float,
        y3: Float,
    ) {
        val point1 = transformable.toDrawOffset(Offset(x1, y1))
        val point2 = transformable.toDrawOffset(Offset(x2, y2))
        val point3 = transformable.toDrawOffset(Offset(x3, y3))
        delegate.cubicTo(point1.x, point1.y, point2.x, point2.y, point3.x, point3.y)
    }

    override fun getBounds(): Rect {
        return delegate.getBounds()
    }

    override fun lineTo(
        x: Float,
        y: Float,
    ) {
        val point = transformable.toDrawOffset(Offset(x, y))
        delegate.lineTo(point.x, point.y)
    }

    override fun moveTo(
        x: Float,
        y: Float,
    ) {
        val point = transformable.toDrawOffset(Offset(x, y))
        delegate.moveTo(point.x, point.y)
    }

    override fun quadraticBezierTo(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
    ) {
        val point1 = transformable.toDrawOffset(Offset(x1, y1))
        val point2 = transformable.toDrawOffset(Offset(x2, y2))
        delegate.quadraticBezierTo(point1.x, point1.y, point2.x, point2.y)
    }

    override fun addPath(
        path: Path,
        offset: Offset,
    ) {
        throw UnsupportedOperationException()
    }

    override fun op(
        path1: Path,
        path2: Path,
        operation: PathOperation,
    ): Boolean {
        throw UnsupportedOperationException()
    }

    override fun relativeCubicTo(
        dx1: Float,
        dy1: Float,
        dx2: Float,
        dy2: Float,
        dx3: Float,
        dy3: Float,
    ) {
        throw UnsupportedOperationException()
    }

    override fun relativeLineTo(
        dx: Float,
        dy: Float,
    ) {
        throw UnsupportedOperationException()
    }

    override fun relativeMoveTo(
        dx: Float,
        dy: Float,
    ) {
        throw UnsupportedOperationException()
    }

    override fun relativeQuadraticBezierTo(
        dx1: Float,
        dy1: Float,
        dx2: Float,
        dy2: Float,
    ) {
        throw UnsupportedOperationException()
    }

    override fun translate(offset: Offset) {
        throw UnsupportedOperationException()
    }
}