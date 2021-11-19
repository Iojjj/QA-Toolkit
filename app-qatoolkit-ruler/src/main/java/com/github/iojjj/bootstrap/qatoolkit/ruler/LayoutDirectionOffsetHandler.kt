package com.github.iojjj.bootstrap.qatoolkit.ruler

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.splineBasedDecay
import androidx.compose.material.ResistanceConfig
import androidx.compose.runtime.State
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import com.github.iojjj.bootstrap.qatoolkit.compose.core.unit.minDimension
import com.github.iojjj.bootstrap.qatoolkit.core.Orientation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

internal class LayoutDirectionOffsetHandler(
    private val coroutineScope: CoroutineScope,
    private val textPositionState: Animatable<Float, AnimationVector1D>,
    private val orientationState: State<Orientation>,
) : DragGestureHandler {

    private val velocityTracker = VelocityTracker()
    private val animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )
    private var initialTextOffset = 0.0f
    private var absoluteOffset = 0.0f
    private var screenSize = 0.0f
    private var slingShoot = false
    private var maxTextOffset = 0f
    private var slingShootOffset = 0f
    private var slingRollbackVelocity = 0f
    private var slingShootVelocity = 0.0f
    private var maxSlingRollbackVelocity = 0.0f
    private lateinit var resistanceConfig: ResistanceConfig

    override val onDragStart: PointerInputScope.() -> Unit = {
        velocityTracker.resetTracking()
        initialTextOffset = when (textPositionState.isRunning) {
            true -> textPositionState.targetValue
            false -> textPositionState.value
        }
        val newScreenSize = when (orientationState.value) {
            Orientation.HORIZONTAL -> size.width.toFloat()
            Orientation.VERTICAL -> size.height.toFloat()
        }
        if (newScreenSize != screenSize) {
            resistanceConfig = ResistanceConfig(newScreenSize)
            screenSize = newScreenSize
        }
        maxTextOffset = screenSize * 0.1f
        slingShootOffset = size.minDimension * 0.5f
        absoluteOffset = 0f
        slingShoot = false
        slingRollbackVelocity = 0.0f
        maxSlingRollbackVelocity = screenSize / 200
        slingShootVelocity = screenSize / 100
    }

    override val onDrag: PointerInputScope.(PointerInputChange, Float) -> Unit = onDrag@{ change, _ ->
        if (slingShoot) {
            return@onDrag
        }
        val dx = change.positionChange().let {
            when (orientationState.value) {
                Orientation.HORIZONTAL -> it.x
                Orientation.VERTICAL -> -it.y
            }
        }
        velocityTracker.addPosition(
            change.uptimeMillis,
            change.position
        )
        absoluteOffset += dx
        if (abs(absoluteOffset) >= slingShootOffset) {
            slingShoot = true
            coroutineScope.launch {
                if (initialTextOffset == OFFSET_END) {
                    textPositionState.animateTo(
                        targetValue = OFFSET_START,
                        animationSpec = animationSpec,
                        initialVelocity = -slingShootVelocity,
                    )
                } else {
                    textPositionState.animateTo(
                        targetValue = OFFSET_END,
                        animationSpec = animationSpec,
                        initialVelocity = +slingShootVelocity,
                    )
                }
            }
        } else {
            slingRollbackVelocity = absoluteOffset / slingShootOffset * maxSlingRollbackVelocity
            val coercedOffset = absoluteOffset.coerceIn(-maxTextOffset, maxTextOffset)
            val overflow = absoluteOffset - coercedOffset
            val resistance = resistanceConfig.computeResistance(overflow)
            val extraTextOffset = (coercedOffset + resistance) / screenSize
            if (initialTextOffset == OFFSET_START && extraTextOffset < 0) {
                coroutineScope.launch {
                    textPositionState.snapTo(OFFSET_START + abs(extraTextOffset))
                }
            } else if (initialTextOffset == OFFSET_END && extraTextOffset > 0) {
                coroutineScope.launch {
                    textPositionState.snapTo(OFFSET_END - extraTextOffset)
                }
            }
        }
    }

    override val onDragEnd: PointerInputScope.() -> Unit = onDragEnd@{
        if (slingShoot) {
            return@onDragEnd
        }
        // No longer receiving touch events. Prepare the animation.
        val velocity = velocityTracker.calculateVelocity().let {
            when (orientationState.value) {
                Orientation.HORIZONTAL -> it.x
                Orientation.VERTICAL -> it.y
            }
        }
        val decay = splineBasedDecay<Float>(this)
        val targetOffset = decay.calculateTargetValue(
            absoluteOffset,
            velocity
        )
        val maxOffset = when (orientationState.value) {
            Orientation.HORIZONTAL -> size.width * 1.0f // 100% is OK
            Orientation.VERTICAL -> size.height * 0.75f // at least 75%
        }
        if (initialTextOffset == OFFSET_START && (
                orientationState.value == Orientation.HORIZONTAL && targetOffset < -maxOffset ||
                    orientationState.value == Orientation.VERTICAL && targetOffset > maxOffset
                )
        ) {
            coroutineScope.launch {
                textPositionState.animateTo(
                    targetValue = OFFSET_END,
                    animationSpec = animationSpec,
                    initialVelocity = +slingShootVelocity,
                )
            }
        } else if (initialTextOffset == OFFSET_END && (
                orientationState.value == Orientation.HORIZONTAL && targetOffset > maxOffset ||
                    orientationState.value == Orientation.VERTICAL && targetOffset < -maxOffset)
        ) {
            coroutineScope.launch {
                textPositionState.animateTo(
                    targetValue = OFFSET_START,
                    animationSpec = animationSpec,
                    initialVelocity = -slingShootVelocity,
                )
            }
        } else {
            // Not enough velocity; Slide back.
            coroutineScope.launch {
                textPositionState.animateTo(
                    targetValue = initialTextOffset,
                    animationSpec = animationSpec,
                    initialVelocity = slingRollbackVelocity
                )
            }
        }
    }
}