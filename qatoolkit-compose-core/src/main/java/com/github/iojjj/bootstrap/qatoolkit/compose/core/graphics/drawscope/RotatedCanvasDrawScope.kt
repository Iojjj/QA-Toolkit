package com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope

internal class RotatedCanvasDrawScope(
    private val delegate: DrawScope,
) : DrawScope by delegate {

    override val size: Size
        get() = delegate.size.let {
            it.copy(width = it.height, height = it.width)
        }
}