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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalPercentFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalSizeFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.geometry.containsInclusive
import com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope.drawTextAboveAndBelow
import com.github.iojjj.bootstrap.qatoolkit.compose.core.graphics.drawscope.drawTextAboveOrBelow
import com.github.iojjj.bootstrap.qatoolkit.core.DimensionType
import com.github.iojjj.bootstrap.qatoolkit.inspector.canvas.TransformableDrawScope
import kotlin.math.abs

@Stable
internal class SizesDrawer(
    textPaintState: State<TextPaint>,
    arrowColorState: State<Color>,
    helperPointsPathEffectState: State<PathEffect>,
    dimensionTypeState: State<DimensionType>,
    isPercentVisibleState: State<Boolean>,
    sizeFormatterState: State<(Float) -> String>,
    percentFormatterState: State<(Float) -> String>,
    screenSizeState: State<IntSize>,
) {
    private val textPaint by textPaintState
    private val arrowColor by arrowColorState
    private val helperPointsPathEffect by helperPointsPathEffectState
    private val dimensionType by dimensionTypeState
    private val isPercentVisible by isPercentVisibleState
    private val sizeFormatter by sizeFormatterState
    private val percentFormatter by percentFormatterState
    private val screenSize by screenSizeState

    @Stable
    fun TransformableDrawScope.drawSizes(
        rootNode: Node,
        pinnedNode: Node?,
        selectedNode: Node?,
    ) {
        if (pinnedNode != null && selectedNode != null) {
            drawSizesBetween(pinnedNode.bounds, selectedNode.bounds)
        } else if (pinnedNode != null) {
            drawSizesBetween(pinnedNode.bounds, rootNode.bounds)
        } else if (selectedNode != null) {
            drawSizesBetween(rootNode.bounds, selectedNode.bounds)
        }
    }

    private fun TransformableDrawScope.drawSizesBetween(
        pinned: Rect,
        selected: Rect,
    ) {
        if (pinned == selected) {
            /* no-op */
            return
        }
        val lines = drawSizesBetween(pinned, selected, screenSize.width.toFloat(), emptyList())
        val rotatedPinned = rotate(pinned)
        val rotatedSelected = rotate(selected)
        vertically {
            drawSizesBetween(rotatedPinned, rotatedSelected, screenSize.height.toFloat(), lines)
        }
    }

    private fun DrawScope.rotate(rect: Rect): Rect {
        val navigationBar = screenSize.height - size.height
        return Rect(
            left = screenSize.height - rect.bottom - navigationBar,
            top = rect.left,
            right = screenSize.height - rect.top - navigationBar,
            bottom = rect.right
        )
    }

    private fun TransformableDrawScope.drawSizesBetween(
        pinned: Rect,
        selected: Rect,
        maxSize: Float,
        existingLines: List<Offset>,
    ): List<Offset> {
        val points = mutableListOf<Offset>()
        val helperPoints = mutableListOf<Offset>()
        val strokeWidth = ARROW_STROKE_WIDTH.toPx()
        getAllPoints(pinned, selected, points, helperPoints)
        val filteredHelperPoints = filterHelperPoints(helperPoints, existingLines)
        drawPoints(points, PointMode.Lines, arrowColor, strokeWidth)
        drawPoints(filteredHelperPoints, PointMode.Lines, arrowColor, strokeWidth, pathEffect = helperPointsPathEffect)
        drawArrowsAndText(points, maxSize)
        return points
    }

    private fun TransformableDrawScope.drawArrowsAndText(
        points: MutableList<Offset>,
        maxSize: Float,
    ) {
        val arrowLength = ARROW_LENGTH.toPx()
        val arrowThickness = ARROW_THICKNESS.toPx()
        val viewportArrowLength = arrowLength / viewportZoom
        val viewportArrowThickness = arrowThickness / viewportZoom
        for (index in 0 until points.size step 2) {
            val start = points[index]
            val end = points[index + 1]
            val viewportStart = toDrawOffset(start)
            val viewportEnd = toDrawOffset(end)
            val length = end.x - start.x
            val viewportLength = viewportEnd.x - viewportStart.x
            val size = when (dimensionType) {
                DimensionType.DP -> length / density
                DimensionType.PX -> length
            }
            val text = sizeFormatter(size)
            val textWidth = textPaint.measureText(text)
            val availableSpace = viewportLength - 2 * arrowLength
            if (textWidth <= availableSpace) {
                drawArrowFits(start, end, viewportArrowLength, viewportArrowThickness)
            } else {
                drawArrowsNotFits(viewportArrowLength, viewportArrowThickness, start, end)
            }
            val offset = (viewportStart + viewportEnd) / 2.0f
            if (isPercentVisible) {
                val textBelow = percentFormatter(size / maxSize)
                drawTextAboveAndBelow(
                    textAbove = text,
                    textBelow = textBelow,
                    textSingleLine = "$text ($textBelow)",
                    textPaint = textPaint,
                    offsetAbove = offset,
                    offsetBelow = offset,
                    minY = 0.0f,
                    maxY = this.size.height,
                )
            } else {
                drawTextAboveOrBelow(text, offset, textPaint, 0.0f, this.size.height)
            }
        }
    }

    private fun TransformableDrawScope.drawArrowFits(
        start: Offset,
        end: Offset,
        arrowLength: Float,
        arrowThickness: Float,
    ) {
        sharedTransformablePath.run {
            reset()
            moveTo(start.x, start.y)
            lineTo(start.x + arrowLength, start.y - arrowThickness)
            lineTo(start.x + arrowLength, start.y + arrowThickness)
            close()
            drawPath(this, arrowColor)
        }
        sharedTransformablePath.run {
            reset()
            moveTo(end.x, end.y)
            lineTo(end.x - arrowLength, start.y - arrowThickness)
            lineTo(end.x - arrowLength, start.y + arrowThickness)
            close()
            drawPath(this, arrowColor)
        }
    }

    private fun TransformableDrawScope.drawArrowsNotFits(
        arrowLength: Float,
        arrowThickness: Float,
        start: Offset,
        end: Offset,
    ) {
        sharedTransformablePath.run {
            reset()
            moveTo(start.x, start.y)
            lineTo(start.x - arrowLength, start.y - arrowThickness)
            lineTo(start.x - arrowLength, start.y + arrowThickness)
            close()
            drawPath(this, arrowColor)
        }
        sharedTransformablePath.run {
            reset()
            moveTo(end.x, end.y)
            lineTo(end.x + arrowLength, start.y - arrowThickness)
            lineTo(end.x + arrowLength, start.y + arrowThickness)
            close()
            drawPath(this, arrowColor)
        }

        val extraArrowStart = Offset(arrowLength, 0.0f)
        val extraArrowEnd = Offset(1.5f * arrowLength, 0.0f)
        drawLine(arrowColor, start - extraArrowStart, start - extraArrowEnd)
        drawLine(arrowColor, end + extraArrowStart, end + extraArrowEnd)
    }

    private fun getAllPoints(
        pinned: Rect,
        selected: Rect,
        points: MutableList<Offset>,
        helperPoints: MutableList<Offset>,
    ) {
        when {
            pinned.containsInclusive(selected.topLeft) && pinned.containsInclusive(selected.bottomRight) -> {
                // Selected is inside of pinned.
                getPointsInsideEachOther(pinned, selected, points)
            }
            selected.containsInclusive(pinned.topLeft) && selected.containsInclusive(pinned.bottomRight) -> {
                // Pinned is inside of selected.
                getPointsInsideEachOther(selected, pinned, points)
            }
            selected.right <= pinned.left -> {
                // Selected is completely to the left of pinned.
                val optimalY = findOptimalYCompletelyOut(pinned, selected)
                if (getPointsCompletelyToLeft(selected, pinned, optimalY, points)) {
                    getHelperPointsCompletelyOut(selected, pinned, optimalY, points.last(), helperPoints)
                }
            }
            selected.left >= pinned.right -> {
                // Selected completely to the right of pinned.
                val optimalY = findOptimalYCompletelyOut(pinned, selected)
                if (getPointsCompletelyToRight(selected, pinned, optimalY, points)) {
                    getHelperPointsCompletelyOut(selected, pinned, optimalY, points[points.lastIndex - 1], helperPoints)
                }
            }
            else -> {
                getPointsToLeft(pinned, selected, points, helperPoints)
                getPointsToRight(pinned, selected, points, helperPoints)
            }
        }
    }

    private fun TransformableDrawScope.filterHelperPoints(
        helperPoints: List<Offset>,
        existingLines: List<Offset>
    ): List<Offset> {
        return when {
            helperPoints.isEmpty() -> {
                emptyList()
            }
            existingLines.isEmpty() -> {
                helperPoints
            }
            else -> {
                val filteredHelperPoints = mutableListOf<Offset>()
                for (helperIndex in helperPoints.indices step 2) {
                    val helperStart = helperPoints[helperIndex]
                    val helperEnd = helperPoints[helperIndex + 1]
                    var matches = false
                    for (lineIndex in existingLines.indices step 2) {
                        val lineStart = existingLines[lineIndex]
                        val lineEnd = existingLines[lineIndex + 1]

                        val rotatedLineStart = Offset(size.width - lineStart.y, lineStart.x)
                        val rotatedLineEnd = Offset(size.width - lineEnd.y, lineEnd.x)
                        matches = matches or (helperStart == rotatedLineStart && helperEnd == rotatedLineEnd)
                        if (matches) {
                            break
                        }
                    }
                    if (!matches) {
                        filteredHelperPoints.add(helperStart)
                        filteredHelperPoints.add(helperEnd)
                    }
                }
                filteredHelperPoints
            }
        }
    }

    private fun getPointsToLeft(
        pinned: Rect,
        selected: Rect,
        points: MutableList<Offset>,
        helperPoints: MutableList<Offset>,
    ) {
        if (selected.left == pinned.left) {
            /* no-op */
        } else if (selected.left < pinned.left) {
            // Selected is partially to the left of pinned.
            val optimalY = findOptimalYPartiallyOut(selected, pinned)
            if (optimalY != null) {
                if (getPointsPartiallyToLeft(selected, pinned, optimalY, points)) {
                    getHelperPointsPartiallyToLeft(selected, optimalY, helperPoints)
                }
            }
        } else {
            // Pinned is partially to the left of selected.
            val optimalY = findOptimalYPartiallyOut(pinned, selected)
            if (optimalY != null) {
                if (getPointsPartiallyToLeft(pinned, selected, optimalY, points)) {
                    getHelperPointsPartiallyToLeft(pinned, optimalY, helperPoints)
                }
            }
        }
    }

    private fun getPointsToRight(
        pinned: Rect,
        selected: Rect,
        points: MutableList<Offset>,
        helperPoints: MutableList<Offset>,
    ) {
        if (selected.right == pinned.right) {
            /* no-op */
        } else if (selected.right > pinned.right) {
            // Selected is partially to the right of pinned.
            val optimalY = findOptimalYPartiallyOut(selected, pinned)
            if (optimalY != null) {
                if (getPointsPartiallyToRight(selected, pinned, optimalY, points)) {
                    getHelperPointsPartiallyToRight(selected, optimalY, helperPoints)
                }
            }
        } else {
            // Pinned is partially to the left of selected.
            val optimalY = findOptimalYPartiallyOut(pinned, selected)
            if (optimalY != null) {
                if (getPointsPartiallyToRight(pinned, selected, optimalY, points)) {
                    getHelperPointsPartiallyToRight(pinned, optimalY, helperPoints)
                }
            }
        }
    }

    private fun getHelperPointsPartiallyToLeft(
        mostLeft: Rect,
        optimalY: Float,
        helperPoints: MutableList<Offset>,
    ) {
        if (optimalY <= mostLeft.top) {
            if (optimalY != mostLeft.top) {
                helperPoints += Offset(mostLeft.left, optimalY)
                helperPoints += Offset(mostLeft.left, mostLeft.top)
            }
        } else {
            if (optimalY != mostLeft.bottom) {
                helperPoints += Offset(mostLeft.left, optimalY)
                helperPoints += Offset(mostLeft.left, mostLeft.bottom)
            }
        }
    }

    private fun getHelperPointsPartiallyToRight(
        mostRight: Rect,
        optimalY: Float,
        helperPoints: MutableList<Offset>,
    ) {
        if (optimalY <= mostRight.top) {
            if (optimalY != mostRight.top) {
                helperPoints += Offset(mostRight.right, optimalY)
                helperPoints += Offset(mostRight.right, mostRight.top)
            }
        } else {
            if (optimalY != mostRight.bottom) {
                helperPoints += Offset(mostRight.right, optimalY)
                helperPoints += Offset(mostRight.right, mostRight.bottom)
            }
        }
    }

    private fun getHelperPointsCompletelyOut(
        most: Rect,
        less: Rect,
        optimalY: Float,
        point: Offset,
        helperPoints: MutableList<Offset>,
    ) {
        if (optimalY !in less.top..less.bottom) {
            if (most.bottom <= less.top) {
                if (optimalY != less.top) {
                    helperPoints += Offset(point.x, optimalY)
                    helperPoints += Offset(point.x, less.top)
                }
            } else {
                if (optimalY != less.bottom) {
                    helperPoints += Offset(point.x, optimalY)
                    helperPoints += Offset(point.x, less.bottom)
                }
            }
        }
    }

    private fun getPointsCompletelyToLeft(
        mostLeft: Rect,
        lessLeft: Rect,
        optimalY: Float,
        points: MutableList<Offset>,
    ): Boolean {
        return if (mostLeft.right != lessLeft.left) {
            points += Offset(mostLeft.right, optimalY)
            points += Offset(lessLeft.left, optimalY)
            true
        } else {
            false
        }
    }

    private fun getPointsCompletelyToRight(
        mostRight: Rect,
        lessRight: Rect,
        optimalY: Float,
        points: MutableList<Offset>,
    ): Boolean {
        return if (mostRight.left != lessRight.right) {
            points += Offset(lessRight.right, optimalY)
            points += Offset(mostRight.left, optimalY)
            true
        } else {
            false
        }
    }

    private fun getPointsPartiallyToLeft(
        mostLeft: Rect,
        lessLeft: Rect,
        optimalY: Float,
        points: MutableList<Offset>,
    ): Boolean {
        return if (mostLeft.left != lessLeft.left) {
            points += Offset(mostLeft.left, optimalY)
            points += Offset(lessLeft.left, optimalY)
            true
        } else {
            false
        }
    }

    private fun getPointsPartiallyToRight(
        mostRight: Rect,
        lessRight: Rect,
        optimalY: Float,
        points: MutableList<Offset>,
    ): Boolean {
        return if (mostRight.right != lessRight.right) {
            points += Offset(lessRight.right, optimalY)
            points += Offset(mostRight.right, optimalY)
            true
        } else {
            false
        }
    }

    private fun getPointsInsideEachOther(
        outer: Rect,
        inner: Rect,
        points: MutableList<Offset>,
    ) {
        // Left
        if (outer.left != inner.left) {
            points += inner.centerLeft.copy(x = outer.left)
            points += inner.centerLeft
        }
        // Right
        if (outer.right != inner.right) {
            points += inner.centerRight
            points += inner.centerRight.copy(x = outer.right)
        }
    }

    private fun findOptimalYCompletelyOut(
        pinned: Rect,
        selected: Rect,
    ): Float {
        return when {
            selected.top in pinned.top..pinned.bottom && selected.bottom in pinned.top..pinned.bottom -> {
                // Selected vertical bounds within pinned vertical bounds
                selected.center.y
            }
            selected.top in pinned.top..pinned.bottom -> {
                // Selected top within pinned vertical bounds.
                (minOf(selected.top, pinned.bottom) + pinned.bottom) / 2
            }
            selected.bottom in pinned.top..pinned.bottom -> {
                // Selected bottom within pinned vertical bounds.
                (maxOf(selected.bottom, pinned.top) + pinned.top) / 2
            }
            selected.bottom <= pinned.top -> {
                // Selected is completely above pinned.
                selected.bottom
            }
            selected.top >= pinned.bottom -> {
                // Selected is completely below pinned.
                selected.top
            }
            pinned.top in selected.top..selected.bottom && pinned.bottom in selected.top..selected.bottom -> {
                // Pinned vertical bounds within selected vertical bounds.
                pinned.center.y
            }
            else -> error("Impossible route. selected: $selected, pinned: $pinned")
        }
    }

    private fun findOptimalYPartiallyOut(
        mostLeft: Rect,
        lessLeft: Rect,
    ): Float? {
        val isTopOverlaps = lessLeft.top in mostLeft.top..mostLeft.bottom
        val isBottomOverlaps = lessLeft.bottom in mostLeft.top..mostLeft.bottom
        return when {
            isTopOverlaps && isBottomOverlaps -> {
                // Most left completely overlaps less left edge.
                null
            }
            isTopOverlaps -> {
                lessLeft.bottom
            }
            isBottomOverlaps -> {
                lessLeft.top
            }
            mostLeft.bottom < lessLeft.top -> {
                lessLeft.top
            }
            mostLeft.top > lessLeft.bottom -> {
                lessLeft.bottom
            }
            else -> {
                val dTop = abs(mostLeft.top - lessLeft.top)
                val dBottom = abs(mostLeft.bottom - lessLeft.bottom)
                if (dTop >= dBottom) {
                    lessLeft.top
                } else {
                    lessLeft.bottom
                }
            }
        }
    }

    companion object {

        private val ARROW_LENGTH = 8.dp
        private val ARROW_THICKNESS = 2.dp
        private val ARROW_STROKE_WIDTH = 1.dp
        internal val HELPER_DASH_ON = 5.dp
        internal val HELPER_DASH_OFF = 2.dp
    }
}

@Composable
internal fun rememberSizesDrawer(
    textPaintState: State<TextPaint>,
    arrowColor: Color,
    dimensionTypeState: State<DimensionType>,
    isPercentVisibleState: State<Boolean>,
    screenSizeState: State<IntSize>,
): SizesDrawer {
    val density = LocalDensity.current
    val helperPointsPathEffect = remember(density) {
        with(density) {
            PathEffect.dashPathEffect(floatArrayOf(SizesDrawer.HELPER_DASH_ON.toPx(), SizesDrawer.HELPER_DASH_OFF.toPx()))
        }
    }
    val arrowColorState = rememberUpdatedState(arrowColor)
    val helperPointsPathEffectState = rememberUpdatedState(helperPointsPathEffect)
    val sizeFormatterState = rememberUpdatedState(LocalSizeFormatter.current)
    val percentFormatterState = rememberUpdatedState(LocalPercentFormatter.current)
    return remember {
        SizesDrawer(
            textPaintState,
            arrowColorState,
            helperPointsPathEffectState,
            dimensionTypeState,
            isPercentVisibleState,
            sizeFormatterState,
            percentFormatterState,
            screenSizeState,
        )
    }
}