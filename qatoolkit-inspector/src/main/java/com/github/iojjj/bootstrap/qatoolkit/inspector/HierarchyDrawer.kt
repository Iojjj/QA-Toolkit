package com.github.iojjj.bootstrap.qatoolkit.inspector

import android.text.TextPaint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalPercentFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalSizeFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope.drawTextInside
import com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope.textHeight
import com.github.iojjj.bootstrap.qatoolkit.core.DimensionType
import com.github.iojjj.bootstrap.qatoolkit.inspector.canvas.TransformableDrawScope
import kotlin.math.abs

@Stable
internal class HierarchyDrawer(
    textPaintState: State<TextPaint>,
    nodeStrokeStyleState: State<DrawStyle>,
    pinnedStrokeStyleState: State<DrawStyle>,
    selectedStrokeStyleState: State<DrawStyle>,
    nodeStrokeColorState: State<Color>,
    pinnedStrokeColorState: State<Color>,
    selectedStrokeColorState: State<Color>,
    dimensionTypeState: State<DimensionType>,
    sizeFormatterState: State<(Float) -> String>,
    isPercentVisibleState: State<Boolean>,
    percentFormatterState: State<(Float) -> String>,
    screenSizeState: State<IntSize>,
) {
    private val textPaint by textPaintState
    private val nodeStyle by nodeStrokeStyleState
    private val pinnedStyle by pinnedStrokeStyleState
    private val selectedStyle by selectedStrokeStyleState
    private val nodeStrokeColor by nodeStrokeColorState
    private val pinnedStrokeColor by pinnedStrokeColorState
    private val selectedStrokeColor by selectedStrokeColorState
    private val dimensionType by dimensionTypeState
    private val sizeFormatter by sizeFormatterState
    private val isPercentVisible by isPercentVisibleState
    private val percentFormatter by percentFormatterState
    private val screenSize by screenSizeState

    @Stable
    fun TransformableDrawScope.drawHierarchy(
        rootNode: Node,
        pinnedNode: Node?,
        selectedNode: Node?,
    ) {
        drawHierarchyInternal(rootNode, pinnedNode, selectedNode)
        exhaustive..when {
            pinnedNode != null && selectedNode != null -> {
                val textHeight = textPaint.textHeight
                val pinnedText = getSizeText(pinnedNode.bounds)
                val pinnedTextWidth = textPaint.measureText(pinnedText)
                val pinnedTextBounds = Rect(
                    offset = toDrawRect(pinnedNode.bounds).center - Offset(pinnedTextWidth / 2, textHeight / 2),
                    size = Size(pinnedTextWidth, textHeight),
                )
                val selectedText = getSizeText(selectedNode.bounds)
                val selectedTextWidth = textPaint.measureText(selectedText)
                val selectedTextBounds = Rect(
                    offset = toDrawRect(selectedNode.bounds).center - Offset(selectedTextWidth / 2, textHeight / 2),
                    size = Size(pinnedTextWidth, textHeight),
                )
                if (!pinnedTextBounds.overlaps(selectedTextBounds)) {
                    // If bounds are not overlap, draw pinned node size.
                    drawSize(pinnedText, pinnedNode.bounds)
                }
                // Always draw selected node size.
                drawSize(selectedText, selectedNode.bounds)
            }
            pinnedNode != null -> {
                drawSize(pinnedNode.bounds)
            }
            selectedNode != null -> {
                drawSize(selectedNode.bounds)
            }
            else -> {
                /* no-op */
            }
        }
    }

    private fun TransformableDrawScope.drawHierarchyInternal(
        rootNode: Node,
        pinnedNode: Node?,
        selectedNode: Node?,
    ) {
        when (rootNode) {
            pinnedNode -> {
                drawNode(rootNode, pinnedStrokeColor, pinnedStyle)
            }
            selectedNode -> {
                drawNode(rootNode, selectedStrokeColor, selectedStyle)
            }
            else -> {
                drawNode(rootNode, nodeStrokeColor, nodeStyle)
            }
        }
        rootNode.children.fastForEach {
            drawHierarchyInternal(it, pinnedNode, selectedNode)
        }
    }

    private fun DrawScope.drawNode(
        node: Node,
        color: Color,
        style: DrawStyle,
    ) {
        drawRect(color, node.bounds.topLeft, node.bounds.size, style = style)
    }

    private fun TransformableDrawScope.drawSize(
        bounds: Rect,
    ) {
        val text = getSizeText(bounds)
        drawSize(text, bounds)
    }

    private fun TransformableDrawScope.drawSize(
        text: String,
        bounds: Rect,
    ) {
        drawTextInside(
            text = text,
            rect = toDrawRect(bounds),
            textPaint = textPaint,
            offset = Offset(x = 0.0f, y = -abs(textPaint.fontMetrics.bottom))
        )
    }

    private fun TransformableDrawScope.getSizeText(bounds: Rect): String {
        val width: Float
        val height: Float
        exhaustive..when (dimensionType) {
            DimensionType.DP -> {
                width = bounds.width / density
                height = bounds.height / density
            }
            DimensionType.PX -> {
                width = bounds.width
                height = bounds.height
            }
        }
        val formattedWidth = sizeFormatter(width)
        val formattedHeight = sizeFormatter(height)
        return if (isPercentVisible) {
            val formattedWidthFraction = percentFormatter(bounds.width / screenSize.width)
            val formattedHeightFraction = percentFormatter(bounds.height / screenSize.height)
            "$formattedWidth ($formattedWidthFraction) x $formattedHeight ($formattedHeightFraction)"
        } else {
            "$formattedWidth x $formattedHeight"
        }
    }

    companion object {

        internal val NODE_STROKE_WIDTH = 0.5.dp
        internal val SELECTED_STROKE_WIDTH = 2.dp
        internal val PINNED_STROKE_WIDTH = 2.dp
    }
}

@Composable
internal fun rememberHierarchyDrawer(
    textPaintState: State<TextPaint>,
    nodeStrokeScale: Float,
    nodeStrokeColor: Color,
    pinnedStrokeColor: Color,
    selectedStrokeColor: Color,
    dimensionTypeState: State<DimensionType>,
    isPercentVisibleState: State<Boolean>,
    screenSizeState: State<IntSize>,
): HierarchyDrawer {
    val density = LocalDensity.current
    val nodeStyle = remember(density, nodeStrokeScale) {
        with(density) {
            Stroke(HierarchyDrawer.NODE_STROKE_WIDTH.toPx() * nodeStrokeScale)
        }
    }
    val pinnedStyle = remember(density, nodeStrokeScale) {
        with(density) {
            Stroke(HierarchyDrawer.PINNED_STROKE_WIDTH.toPx() * nodeStrokeScale)
        }
    }
    val selectedStyle = remember(density, nodeStrokeScale) {
        with(density) {
            Stroke(HierarchyDrawer.SELECTED_STROKE_WIDTH.toPx() * nodeStrokeScale)
        }
    }
    val nodeStrokeStyleState = rememberUpdatedState(nodeStyle)
    val selectedStrokeStyleState = rememberUpdatedState(selectedStyle)
    val pinnedStrokeStyleState = rememberUpdatedState(pinnedStyle)
    val nodeStrokeColorState = rememberUpdatedState(nodeStrokeColor)
    val selectedStrokeColorState = rememberUpdatedState(selectedStrokeColor)
    val pinnedStrokeColorState = rememberUpdatedState(pinnedStrokeColor)
    val sizeFormatterState = rememberUpdatedState(LocalSizeFormatter.current)
    val percentFormatterState = rememberUpdatedState(LocalPercentFormatter.current)
    return remember {
        HierarchyDrawer(
            textPaintState,
            nodeStrokeStyleState,
            pinnedStrokeStyleState,
            selectedStrokeStyleState,
            nodeStrokeColorState,
            pinnedStrokeColorState,
            selectedStrokeColorState,
            dimensionTypeState,
            sizeFormatterState,
            isPercentVisibleState,
            percentFormatterState,
            screenSizeState,
        )
    }
}