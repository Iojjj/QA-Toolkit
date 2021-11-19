@file:Suppress("DeprecatedCallableAddReplaceWith")

package com.github.iojjj.bootstrap.qatoolkit.inspector.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation

interface TransformablePath : Path {

    val delegate: Path

    @Deprecated(
        message = "Unsupported.",
        level = DeprecationLevel.ERROR
    )
    override fun addPath(
        path: Path,
        offset: Offset,
    ) {
        throw UnsupportedOperationException()
    }

    @Deprecated(
        message = "Unsupported.",
        level = DeprecationLevel.ERROR
    )
    override fun op(
        path1: Path,
        path2: Path,
        operation: PathOperation,
    ): Boolean {
        throw UnsupportedOperationException()
    }

    @Deprecated(
        message = "Unsupported.",
        level = DeprecationLevel.ERROR
    )
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

    @Deprecated(
        message = "Unsupported.",
        level = DeprecationLevel.ERROR
    )
    override fun relativeLineTo(
        dx: Float,
        dy: Float,
    ) {
        throw UnsupportedOperationException()
    }

    @Deprecated(
        message = "Unsupported.",
        level = DeprecationLevel.ERROR
    )
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

    @Deprecated(
        message = "Unsupported.",
        level = DeprecationLevel.ERROR
    )
    override fun translate(offset: Offset) {
        throw UnsupportedOperationException()
    }
}