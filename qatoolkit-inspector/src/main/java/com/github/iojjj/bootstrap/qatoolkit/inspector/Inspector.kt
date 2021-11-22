package com.github.iojjj.bootstrap.qatoolkit.inspector

import android.text.TextPaint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalScreenSize
import com.github.iojjj.bootstrap.qatoolkit.core.DimensionType
import com.github.iojjj.bootstrap.qatoolkit.inspector.canvas.TransformableCanvas
import com.github.iojjj.bootstrap.qatoolkit.inspector.canvas.TransformableCanvasState
import com.github.iojjj.bootstrap.qatoolkit.inspector.canvas.TransformableDrawScope
import com.github.iojjj.bootstrap.qatoolkit.inspector.canvas.rememberTransformableCanvasState

private val TEXT_SIZE = 12.sp

@Composable
fun Inspector(
    rootNode: Node,
    transformableCanvasState: TransformableCanvasState,
    modifier: Modifier,
    pinnedNode: Node? = null,
    selectedNode: Node? = null,
    overlayColor: Color = Color.White.copy(alpha = 0.75f),
    arrowColor: Color = Color.Black,
    textScale: Float = 1.0f,
    textColor: Color = Color.Black,
    nodeStrokeScale: Float = 1.0f,
    nodeStrokeColor: Color = Color.Black.copy(alpha = 0.5f),
    pinnedStrokeColor: Color = Color.Red.copy(alpha = 0.5f),
    selectedStrokeColor: Color = Color.Blue.copy(alpha = 0.5f),
    dimensionType: DimensionType,
    isPercentVisible: Boolean,
) {
    val rootNodeState = rememberUpdatedState(rootNode)
    val pinnedNodeState = rememberUpdatedState(pinnedNode)
    val selectedNodeState = rememberUpdatedState(selectedNode)
    val dimensionTypeState = rememberUpdatedState(dimensionType)
    val isPercentVisibleState = rememberUpdatedState(isPercentVisible)
    val screenSizeState = rememberUpdatedState(LocalScreenSize.current)
    val textPaintState = rememberUpdatedTextPaintState(textScale, textColor)
    val hierarchyDrawer = rememberHierarchyDrawer(
        textPaintState,
        nodeStrokeScale,
        nodeStrokeColor,
        pinnedStrokeColor,
        selectedStrokeColor,
        dimensionTypeState,
        isPercentVisibleState,
        screenSizeState,
    )
    val sizesDrawer = rememberSizesDrawer(
        textPaintState,
        arrowColor,
        dimensionTypeState,
        isPercentVisibleState,
        screenSizeState,
    )
    val overlayColorState = animateColorAsState(
        when (transformableCanvasState.zoom == 1.0f) {
            true -> overlayColor
            else -> overlayColor.copy(alpha = 1.0f)
        }
    )

    val onDraw = remember<TransformableDrawScope.() -> Unit> {
        {
            drawRect(overlayColorState.value)
            with(hierarchyDrawer) {
                drawHierarchy(rootNodeState.value, pinnedNodeState.value, selectedNodeState.value)
            }
            with(sizesDrawer) {
                drawSizes(rootNodeState.value, pinnedNodeState.value, selectedNodeState.value)
            }
        }
    }
    TransformableCanvas(
        state = transformableCanvasState,
        onDraw = onDraw,
        modifier = modifier
    )
}

@Composable
private fun rememberUpdatedTextPaintState(
    textScale: Float,
    textColor: Color
): State<TextPaint> {
    val textPaint = remember {
        mutableStateOf(
            TextPaint(TextPaint.ANTI_ALIAS_FLAG),
            policy = neverEqualPolicy()
        )
    }
    val density = LocalDensity.current
    val newTextSize = with(density) {
        TEXT_SIZE.toPx() * textScale
    }
    val newTextColor = textColor.toArgb()
    if (textPaint.value.color != newTextColor || textPaint.value.textSize != newTextSize) {
        textPaint.value = textPaint.value.apply {
            color = newTextColor
            textSize = newTextSize
        }
    }
    return textPaint
}

private class PreviewNode(
    override val bounds: Rect = Size(400.0f, 400.0f).toRect(),
    override val children: List<Node> = emptyList(),
) : Node

@Preview
@Composable
private fun PreviewInspector() {
    Inspector(
        rootNode = PreviewNode(),
        transformableCanvasState = rememberTransformableCanvasState(10.0f),
        modifier = Modifier.fillMaxSize(),
        dimensionType = DimensionType.DP,
        isPercentVisible = false,
    )
}

