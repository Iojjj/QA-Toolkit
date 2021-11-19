package com.github.iojjj.bootstrap.qatoolkit.toolkitbar

import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type.displayCutout
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.pub.core.logger.Logger
import com.github.iojjj.bootstrap.pub.core.view.delayUntilNextLayout
import com.github.iojjj.bootstrap.qatoolkit.compose.core.unit.maxDimension
import com.github.iojjj.bootstrap.qatoolkit.core.Orientation
import com.github.iojjj.bootstrap.qatoolkit.grid.GridModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.initial.InitialModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.inspector.InspectorModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.overlay.ContentChange
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitContentOverlayManager.Companion.adjustPositionForRotation
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitContentOverlayManager.Companion.adjustPositionInBounds
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitContentOverlayManager.Companion.isEmpty
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitContentOverlayManager.Companion.isOnlyTopChanged
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitMode
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.route
import com.github.iojjj.bootstrap.qatoolkit.ruler.RulerModeViewModel
import com.github.iojjj.bootstrap.qatoolkit.scopedViewModel
import com.google.accompanist.insets.Insets
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ToolkitBarOverlay(
    navController: NavHostController,
    viewModelFactory: ViewModelProvider.Factory,
    callback: @DisallowComposableCalls (contentChange: ContentChange) -> Unit,
) {
    val viewModel = viewModel<ToolkitBarViewModel>(factory = viewModelFactory)
    val orientationState = viewModel.orientation.collectAsState()
    val animatableOffset = remember {
        Animatable(Offset(viewModel.offsetX.value, viewModel.offsetY.value), Offset.VectorConverter)
    }
    val boxOffset = remember {
        Animatable(Offset.Zero, Offset.VectorConverter)
    }
    val orientationTransition = updateTransition(
        targetState = orientationState.value,
        label = "Orientation Transition"
    )
    val localOrientationState = rememberUpdatedState(orientationTransition.currentState)
    val isReadyState = remember {
        mutableStateOf(false)
    }

    val menuViewModel = rememberViewModel(navController, viewModelFactory)
    val menuItemsState = menuViewModel.menuItems.collectAsState()
    val isToolkitBarVisible = rememberToolkitBarVisible(menuViewModel)

    val onOrientationChangeState = rememberUpdatedState(viewModel.onOrientationChange)
    val screenBoundsState = rememberScreenBoundsState()
    val toolkitBarSizeState = remember {
        mutableStateOf(IntSize.Zero)
    }
    val localOnOrientationChange = rememberOnOrientationChange(
        localOrientationState,
        screenBoundsState,
        animatableOffset,
        onOrientationChangeState,
        toolkitBarSizeState,
        callback
    )
    val onSizeChanged = rememberOnSizeChanged(
        localOrientationState,
        screenBoundsState,
        toolkitBarSizeState,
        animatableOffset,
    )
    val animationOffset = remember<(Int) -> Int>(localOrientationState.value) {
        when (localOrientationState.value) {
            Orientation.HORIZONTAL -> {
                { height ->
                    if (animatableOffset.value.y == screenBoundsState.value.top.toFloat()) {
                        -height
                    } else {
                        height
                    }
                }
            }
            Orientation.VERTICAL -> {
                { width ->
                    if (animatableOffset.value.x == screenBoundsState.value.left.toFloat()) {
                        -width
                    } else {
                        width
                    }
                }
            }
        }
    }
    val enterTransition = remember(localOrientationState.value) {
        when (localOrientationState.value) {
            Orientation.HORIZONTAL -> {
                slideInVertically(animationOffset, tween(delayMillis = DefaultDurationMillis))
            }
            Orientation.VERTICAL -> {
                slideInHorizontally(animationOffset, tween(delayMillis = DefaultDurationMillis))
            }
        } + fadeIn(animationSpec = tween(delayMillis = DefaultDurationMillis))
    }
    val exitTransition = remember(localOrientationState.value) {
        when (localOrientationState.value) {
            Orientation.HORIZONTAL -> {
                slideOutVertically(animationOffset)
            }
            Orientation.VERTICAL -> {
                slideOutHorizontally(animationOffset)
            }
        } + fadeOut()
    }
    AnimatedVisibility(
        visible = isToolkitBarVisible,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        val alpha = when (isReadyState.value) {
            true -> 1.0f
            false -> 0.0f
        }
        Box(
            modifier = Modifier
                .alpha(alpha)
                .offset {
                    IntOffset(boxOffset.value.x.roundToInt(), boxOffset.value.y.roundToInt())
                }
        ) {
            val onDragHandler = rememberOnDragHandler(
                localOrientationState,
                screenBoundsState,
                toolkitBarSizeState,
                animatableOffset,
            )
            ToolkitBar(
                orientationTransition = orientationTransition,
                onDragHandler = onDragHandler,
                menuItems = menuItemsState.value,
                onMenuItemClick = menuViewModel.onMenuItemClick as (ToolkitBarMenuItem) -> Unit,
                onOrientationChange = localOnOrientationChange,
                modifier = Modifier.onSizeChanged(onSizeChanged)
            )
        }
    }

    InitializeWindowLocationEffect(
        localOrientationState,
        animatableOffset,
        boxOffset,
        toolkitBarSizeState,
        screenBoundsState,
        isReadyState
    )
    UpdateWindowLocationEffect(animatableOffset.value, viewModel, callback)
}

@Composable
private fun rememberOnDragHandler(
    orientationState: State<Orientation>,
    screenBoundsState: State<IntRect>,
    toolkitBarSizeState: MutableState<IntSize>,
    animatableOffset: Animatable<Offset, AnimationVector2D>,
): suspend PointerInputScope.() -> Unit {
    val coroutineScope = rememberCoroutineScope()
    return remember {
        {
            val decay = splineBasedDecay<Offset>(this)
            val velocityTracker = VelocityTracker()
            detectDragGestures(
                onDragStart = {
                    velocityTracker.resetTracking()
                },
                onDragEnd = {
                    val orientation = orientationState.value
                    val bounds = screenBoundsState.value
                    val toolkitBarSize = toolkitBarSizeState.value
                    val velocity = velocityTracker.calculateVelocity().let {
                        Offset(it.x, it.y)
                    }
                    val left = bounds.left.toFloat()
                    val right = (bounds.right - toolkitBarSize.width).coerceAtLeast(bounds.left).toFloat()
                    val top = bounds.top.toFloat()
                    val bottom = (bounds.bottom - toolkitBarSize.height).coerceAtLeast(bounds.top).toFloat()
                    val targetOffset = decay.calculateTargetValue(Offset.VectorConverter, animatableOffset.value, velocity)
                    var newX = targetOffset.x.coerceIn(left, right)
                    var newY = targetOffset.y.coerceIn(top, bottom)

                    exhaustive..when (orientation) {
                        Orientation.HORIZONTAL -> {
                            val centerY = newY + toolkitBarSize.height / 2
                            newY = if (centerY - top <= bottom - centerY) {
                                top
                            } else {
                                bottom
                            }
                        }
                        Orientation.VERTICAL -> {
                            val centerX = newX + toolkitBarSize.width / 2
                            newX = if (centerX - left <= right - centerX) {
                                left
                            } else {
                                right
                            }
                        }
                    }

                    coroutineScope.launch {
                        animatableOffset.animateTo(Offset(newX, newY), initialVelocity = velocity)
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consumeAllChanges()
                    val bounds = screenBoundsState.value
                    val toolkitBarSize = toolkitBarSizeState.value

                    velocityTracker.addPosition(
                        change.uptimeMillis,
                        change.position
                    )

                    val left = bounds.left.toFloat()
                    val right = (bounds.right - toolkitBarSize.width).coerceAtLeast(bounds.left).toFloat()
                    val top = bounds.top.toFloat()
                    val bottom = (bounds.bottom - toolkitBarSize.height).coerceAtLeast(bounds.top).toFloat()
                    coroutineScope.launch {
                        animatableOffset.snapTo(
                            Offset(
                                (animatableOffset.value.x + dragAmount.x).coerceIn(left, right),
                                (animatableOffset.value.y + dragAmount.y).coerceIn(top, bottom),
                            )
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun rememberToolkitBarVisible(
    viewModel: ToolkitModeViewModel<out Any>,
): Boolean {
    val inspectorModeViewModel = viewModel as? InspectorModeViewModel
        ?: return true
    val nodeDetailsState = inspectorModeViewModel.details.collectAsState()
    return nodeDetailsState.value == null
}

@Composable
private fun rememberViewModel(
    navController: NavHostController,
    viewModelFactory: ViewModelProvider.Factory
): ToolkitModeViewModel<out Any> {
    val backStackEntry = navController.currentBackStackEntryFlow.collectAsState(navController.currentBackStackEntry).value
    return when (backStackEntry?.route<ToolkitMode>()) {
        ToolkitMode.Initial -> backStackEntry.scopedViewModel<InitialModeViewModel>(factory = viewModelFactory)
        ToolkitMode.Inspect -> backStackEntry.scopedViewModel<InspectorModeViewModel>(factory = viewModelFactory)
        ToolkitMode.Grid -> backStackEntry.scopedViewModel<GridModeViewModel>(factory = viewModelFactory)
        ToolkitMode.Ruler -> backStackEntry.scopedViewModel<RulerModeViewModel>(factory = viewModelFactory)
        null -> error("")
    }
}

@Composable
private fun rememberOnSizeChanged(
    orientationState: State<Orientation>,
    screenBoundsState: State<IntRect>,
    toolkitBarSizeState: MutableState<IntSize>,
    offset: Animatable<Offset, AnimationVector2D>,
): (IntSize) -> Unit {
    val coroutineScope = rememberCoroutineScope()
    return remember {
        { toolkitBarSize ->
            toolkitBarSizeState.value = toolkitBarSize
            coroutineScope.launch {
                adjustPositionInBounds(
                    orientationState.value,
                    screenBoundsState.value,
                    toolkitBarSize,
                    offset,
                )?.join()
            }
        }
    }
}

@Composable
private fun rememberOnOrientationChange(
    orientationState: State<Orientation>,
    screenBoundsState: State<IntRect>,
    offset: Animatable<Offset, AnimationVector2D>,
    onOrientationChangeState: State<(Orientation) -> Unit>,
    toolkitBarSizeState: State<IntSize>,
    callback: @DisallowComposableCalls (contentChange: ContentChange) -> Unit,
): (Orientation) -> Unit {
    val coroutineScope = rememberCoroutineScope()
    val viewState = rememberUpdatedState(LocalView.current)
    return remember {
        onOrientationChange@{ newOrientation ->
            if (orientationState.value == newOrientation) {
                return@onOrientationChange
            }
            val toolkitBarSize = toolkitBarSizeState.value
            val view = viewState.value
            coroutineScope.launch {
                // Wait for animation end before updating layout, otherwise glitches happen.
                adjustPositionForRotation(newOrientation, screenBoundsState.value, toolkitBarSize, offset)
                val tempSize = toolkitBarSize.maxDimension
                callback(ContentChange.ChangeSize(tempSize, tempSize))
                view.delayUntilNextLayout()
                onOrientationChangeState.value.invoke(newOrientation)
                while (orientationState.value != newOrientation) {
                    awaitFrame()
                }
                callback(ContentChange.ChangeSize(WRAP_CONTENT, WRAP_CONTENT))
                view.requestLayout()
            }
        }
    }
}

@Composable
private fun InitializeWindowLocationEffect(
    orientationState: State<Orientation>,
    animatableOffset: Animatable<Offset, AnimationVector2D>,
    boxOffset: Animatable<Offset, AnimationVector2D>,
    toolkitBarSizeState: State<IntSize>,
    screenBoundsState: State<IntRect>,
    isReadyState: MutableState<Boolean>,
) {
    val toolkitBarSize = toolkitBarSizeState.value
    val screenBounds = screenBoundsState.value
    LaunchedEffect(toolkitBarSize, screenBounds) {
        val offsetX = animatableOffset.value.x
        val offsetY = animatableOffset.value.y
        if (!isReadyState.value && toolkitBarSize != IntSize.Zero && screenBounds != IntRect.Zero) {
            val initializeLocation = offsetX < 0 || offsetY < 0
            exhaustive..when (orientationState.value) {
                Orientation.HORIZONTAL -> {
                    if (initializeLocation) {
                        animatableOffset.snapTo(
                            Offset(
                                screenBounds.center.x - toolkitBarSize.width / 2.0f,
                                screenBounds.top.toFloat()
                            )
                        )
                    }
                    val initialOffset = if (offsetY == screenBounds.top.toFloat()) {
                        Offset(
                            0.0f,
                            -(toolkitBarSize.height + screenBounds.top).toFloat(),
                        )
                    } else {
                        Offset(
                            0.0f,
                            (toolkitBarSize.height + screenBounds.bottom).toFloat(),
                        )
                    }
                    boxOffset.snapTo(initialOffset)
                    isReadyState.value = true
                    boxOffset.animateTo(initialOffset.copy(y = 0.0f))
                }
                Orientation.VERTICAL -> {
                    if (initializeLocation) {
                        animatableOffset.snapTo(
                            Offset(
                                screenBounds.left.toFloat(),
                                screenBounds.center.y - toolkitBarSize.height / 2.0f
                            )
                        )
                    }
                    val initialOffset = if (offsetX == screenBounds.left.toFloat()) {
                        Offset(
                            -(toolkitBarSize.width + screenBounds.left).toFloat(),
                            0.0f
                        )
                    } else {
                        Offset(
                            (toolkitBarSize.width + screenBounds.right).toFloat(),
                            0.0f
                        )
                    }
                    boxOffset.snapTo(initialOffset)
                    isReadyState.value = true
                    boxOffset.animateTo(initialOffset.copy(x = 0.0f))
                }
            }
        }
    }
}

@Composable
private fun UpdateWindowLocationEffect(
    offset: Offset,
    viewModel: ToolkitBarViewModel,
    callback: @DisallowComposableCalls (contentChange: ContentChange) -> Unit,
) {
    LaunchedEffect(offset) {
        callback(
            ContentChange.UpdatePosition(
                x = offset.x.roundToInt(),
                y = offset.y.roundToInt(),
            )
        )
        viewModel.onOffsetXChange(offset.x)
        viewModel.onOffsetYChange(offset.y)
    }
}

@Composable
private fun rememberScreenBoundsState(): State<IntRect> {
    val view = LocalView.current
    return remember(view) {
        var okInsets = Insets.Empty
        val boundsState = mutableStateOf(IntRect.Zero)
        val displayMetrics = DisplayMetrics()
        val point = Point()
        val windowManager = view.context.getSystemService<WindowManager>()!!
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        windowManager.defaultDisplay.getSize(point)
        val screenSize = IntSize(displayMetrics.widthPixels, displayMetrics.heightPixels)
        val contentSize = IntSize(point.x, point.y)

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val displayCutout = insets.getInsetsIgnoringVisibility(displayCutout())
            val statusBars = insets.getInsetsIgnoringVisibility(statusBars())
            val affectedInsets = androidx.core.graphics.Insets.max(displayCutout, statusBars)
            Logger.DEFAULT.debug { "insets: $affectedInsets" }
            val newInsets = Insets.Insets(affectedInsets.left, affectedInsets.top, affectedInsets.right, affectedInsets.bottom)
            if (newInsets.isOnlyTopChanged(okInsets) && !okInsets.isEmpty()) {
                // Skip
            } else {
                if (okInsets.isEmpty()) {
                    okInsets = newInsets
                }
                boundsState.value = IntRect(
                    left = newInsets.left,
                    top = newInsets.top,
                    right = screenSize.width - newInsets.right,
                    bottom = screenSize.height - affectedInsets.top,
                )
            }
            insets
        }
        boundsState
    }
}