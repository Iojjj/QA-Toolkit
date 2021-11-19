package com.github.iojjj.bootstrap.qatoolkit.ruler

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import com.github.iojjj.bootstrap.qatoolkit.core.Orientation

@Stable
internal class UnpinnedPositionHandler(
    orientationState: State<Orientation>,
    unpinnedPositionState: State<Float>,
    onUnpinnedPositionChangeState: State<(Float) -> Unit>,
    sensitivityState: State<Float>,
) : DragGestureHandler {

    private val orientation by orientationState
    private val unpinnedPosition by unpinnedPositionState
    private val sensitivity by sensitivityState
    private val onUnpinnedPositionChange by onUnpinnedPositionChangeState

    @Stable
    override val onDragStart: PointerInputScope.() -> Unit = {
        /* no-op */
    }

    @Stable
    override val onDragEnd: PointerInputScope.() -> Unit = {
        /* no-op */
    }

    @Stable
    override val onDrag: PointerInputScope.(PointerInputChange, Float) -> Unit = { _, dragAmount ->
        val max = when (orientation) {
            Orientation.HORIZONTAL -> size.height.toFloat()
            Orientation.VERTICAL -> size.width.toFloat()
        }
        val newPosition = (unpinnedPosition + dragAmount * sensitivity).coerceIn(0.0f, max)
        onUnpinnedPositionChange(newPosition)
    }
}