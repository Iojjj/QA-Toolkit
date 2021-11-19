@file:Suppress("DeprecatedCallableAddReplaceWith")

package com.github.iojjj.bootstrap.qatoolkit.inspector.canvas

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill

interface TransformableDrawScope :
    DrawScope,
    Transformable {

    val viewportSize: Size
    val viewportZoom: Float
    val sharedTransformablePath: TransformablePath

    fun newTransformablePath(): TransformablePath

    fun drawPath(
        path: TransformablePath,
        color: Color,
        alpha: Float = 1.0f,
        style: DrawStyle = Fill,
        colorFilter: ColorFilter? = null,
        blendMode: BlendMode = DrawScope.DefaultBlendMode,
    )

    fun drawPath(
        path: TransformablePath,
        brush: Brush,
        alpha: Float = 1.0f,
        style: DrawStyle = Fill,
        colorFilter: ColorFilter? = null,
        blendMode: BlendMode = DrawScope.DefaultBlendMode,
    )

    fun vertically(onDraw: TransformableDrawScope.() -> Unit)
}