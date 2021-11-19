package com.github.iojjj.bootstrap.qatoolkit.ruler

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Stable
internal class RulerLinesDrawer(
    unpinnedPositionState: State<Float>,
    onUnpinnedPositionChangeState: State<(Float) -> Unit>,
    pinnedPositionsState: State<List<Float>>,
    strokeScaleState: State<Float>,
    strokeColorState: State<Color>,
) {

    private val unpinnedPosition by unpinnedPositionState
    private val onUnpinnedPositionChange by onUnpinnedPositionChangeState
    private val pinnedPositions by pinnedPositionsState
    private val strokeScale by strokeScaleState
    private val strokeColor by strokeColorState

    @Stable
    fun draw(drawScope: DrawScope) = with(drawScope) {
        val width = size.width
        val height = size.height

        if (unpinnedPosition.isNaN()) {
            onUnpinnedPositionChange(height / 2)
        }

        val points = mutableListOf<Offset>()
        pinnedPositions.fastForEach { pinnedPosition ->
            points.add(Offset(0.0f, pinnedPosition))
            points.add(Offset(width, pinnedPosition))
        }
        // Pinned rulers
        drawPoints(
            points = points,
            pointMode = PointMode.Lines,
            strokeWidth = PINNED_STROKE_WIDTH.toPx() * strokeScale,
            color = strokeColor,
            alpha = PINNED_STROKE_ALPHA,
        )
        // Unpinned ruler
        drawLine(
            start = Offset(0.0f, unpinnedPosition),
            end = Offset(width, unpinnedPosition),
            strokeWidth = UNPINNED_STROKE_WIDTH.toPx() * strokeScale,
            color = strokeColor,
        )
        points
    }

    companion object {

        private val UNPINNED_STROKE_WIDTH = 1.dp
        private val PINNED_STROKE_WIDTH = 0.75.dp
        private const val PINNED_STROKE_ALPHA = 0.75f
    }
}

@Composable
internal fun rememberRulerLinesDrawer(
    pinnedPositions: List<Float>,
    onUnpinnedPositionChange: (Float) -> Unit,
    strokeScale: Float,
    strokeColor: Color,
    unpinnedPositionState: State<Float>
): RulerLinesDrawer {
    val pinnedPositionsState = remember {
        mutableStateOf(emptyList<Float>())
    }
    val onUnpinnedPositionChangeState = rememberUpdatedState(onUnpinnedPositionChange)
    val strokeScaleState = rememberUpdatedState(strokeScale)
    val strokeColorState = rememberUpdatedState(strokeColor)
    val rulerDrawer = remember {
        RulerLinesDrawer(
            unpinnedPositionState,
            onUnpinnedPositionChangeState,
            pinnedPositionsState,
            strokeScaleState,
            strokeColorState,
        )
    }
    LaunchedEffect(pinnedPositions) {
        withContext(Dispatchers.Default) {
            pinnedPositionsState.value = pinnedPositions.sorted()
        }
    }
    return rulerDrawer
}