package com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate

fun DrawScope.vertically(
    onDraw: DrawScope.() -> Unit,
) {
    rotate(-90f, pivot = Offset.Zero) {
        translate(left = -size.height) {
            RotatedCanvasDrawScope(this).apply(onDraw)
        }
    }
}
