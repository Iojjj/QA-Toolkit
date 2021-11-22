package com.github.iojjj.bootstrap.qatoolkit.toolkitbar

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalHasBridge
import com.github.iojjj.bootstrap.qatoolkit.core.Orientation
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.style.ToolkitBarStyle

internal val OneItemSize = 36.dp
internal val BoxPadding = 8.dp

@Composable
fun <T : ToolkitBarMenuItem> ToolkitBar(
    menuItems: List<T>,
    onMenuItemClick: (T) -> Unit,
    orientationTransition: Transition<Orientation>,
    onOrientationChange: (newOrientation: Orientation) -> Unit,
    onDragHandler: suspend PointerInputScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    val onOrientationChangeState = rememberUpdatedState(onOrientationChange)

    val transitionSpec = remember<@Composable Transition.Segment<Orientation>.() -> FiniteAnimationSpec<Float>> {
        {
            spring(dampingRatio = Spring.DampingRatioLowBouncy)
        }
    }
    val verticalToHorizontalRotation = orientationTransition.animateFloat(
        label = "Vertical to Horizontal Rotation",
        transitionSpec = transitionSpec
    ) { state ->
        when (state) {
            Orientation.VERTICAL -> 0.0f
            Orientation.HORIZONTAL -> 90.0f
        }
    }
    val horizontalToVerticalRotation = orientationTransition.animateFloat(
        label = "Horizontal to Vertical Rotation",
        transitionSpec = transitionSpec
    ) { state ->
        when (state) {
            Orientation.HORIZONTAL -> 0.0f
            Orientation.VERTICAL -> -90.0f
        }
    }
    val isInteractionEnabled = !orientationTransition.isRunning

    val onRotateClick = rememberOnRotateClick(orientationTransition, onOrientationChangeState)
    val localOnDragHandler = rememberDragHandler(isInteractionEnabled, onDragHandler)

    val listState = rememberLazyListState()
    val listSize = rememberListSize(menuItems.size)
    val animatedListSize = animateDpAsState(listSize)
    val currentOrientation = orientationTransition.currentState
    val rotationOrigin = rememberRotationOrigin(currentOrientation, listSize)
    val rotationDegrees: Float = when (currentOrientation) {
        Orientation.HORIZONTAL -> horizontalToVerticalRotation.value
        Orientation.VERTICAL -> verticalToHorizontalRotation.value
    }
    val listContainer = rememberListContainerFactory(currentOrientation)
    val list = rememberListFactory(currentOrientation, listState, animatedListSize)
    val divider = rememberDividerFactory(currentOrientation)
    Box(
        modifier = Modifier
            .then(modifier)
            .padding(BoxPadding)
    ) {
        Card(
            elevation = ToolkitBarStyle.CardElevation,
            modifier = Modifier
                .cardSize(currentOrientation)
                .graphicsLayer {
                    transformOrigin = rotationOrigin
                    rotationZ = -rotationDegrees
                },
        ) {
            listContainer {
                Spacer(Modifier.size(OneItemSize))
                divider()
                if (menuItems.isNotEmpty()) {
                    list {
                        items(menuItems) { menuItem ->
                            ListItem(menuItem, rotationDegrees, isInteractionEnabled, onMenuItemClick)
                        }
                    }
                    divider()
                }
                ToolkitBarRotateItem(
                    modifier = Modifier.rotateItemRotation(currentOrientation),
                    isClickEnabled = isInteractionEnabled,
                    onClick = onRotateClick
                )
            }
        }
        Box {
            ToolkitBarDragItem(isInteractionEnabled, localOnDragHandler)
            if (LocalHasBridge.current) {
                Icon(
                    painter = painterResource(R.drawable.qatoolkit_toolkitbar_ic_bridge_connected_10dp),
                    contentDescription = null,
                    tint = ToolkitBarStyle.Colors.onBackground,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(2.dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Composable
private fun <T : ToolkitBarMenuItem> ListItem(
    menuItem: T,
    rotationDegrees: Float,
    isInteractionEnabled: Boolean,
    onMenuItemClick: (T) -> Unit,
) {
    val interactionModifier = when (menuItem is Checkable) {
        true -> {
            Modifier.toggleable(
                value = menuItem.isChecked,
                enabled = isInteractionEnabled && menuItem.isEnabled,
                role = Role.Switch,
                onValueChange = {
                    onMenuItemClick(menuItem)
                }
            )
        }
        false -> {
            Modifier.clickable(
                enabled = isInteractionEnabled && menuItem.isEnabled,
                onClick = {
                    onMenuItemClick(menuItem)
                }
            )
        }
    }
    val isChecked = menuItem is Checkable && menuItem.isChecked
    val tintColor = animateColorAsState(
        ToolkitBarStyle.Colors.contentColor(
            isChecked = isChecked,
            isEnabled = menuItem.isEnabled
        )
    )
    val modifier = Modifier
        .graphicsLayer {
            rotationZ = rotationDegrees
        }
        .then(interactionModifier)
    exhaustive..when (val item = menuItem as ToolkitBarMenuItem) {
        is ToolkitBarMenuItem.IconOnly -> {
            ToolkitBarItemIcon(
                icon = item.icon,
                contentColor = tintColor.value,
                modifier = modifier,
            )
        }
        is ToolkitBarMenuItem.TextOnly -> {
            ToolkitBarItemText(
                text = item.text,
                contentColor = tintColor.value,
                modifier = modifier
            )
        }
        is ToolkitBarMenuItem.IconText -> {
            ToolkitBarItemIconText(
                icon = item.icon,
                text = item.text,
                contentColor = tintColor.value,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun rememberOnRotateClick(
    orientationTransition: Transition<Orientation>,
    onOrientationChangeState: State<(newOrientation: Orientation) -> Unit>,
): () -> Unit {
    return remember {
        {
            if (!orientationTransition.isRunning) {
                onOrientationChangeState.value.invoke(
                    when (orientationTransition.currentState) {
                        Orientation.HORIZONTAL -> Orientation.VERTICAL
                        Orientation.VERTICAL -> Orientation.HORIZONTAL
                    }
                )
            }
        }
    }
}

@Composable
private fun rememberDragHandler(
    isInteractionEnabled: Boolean,
    onDragHandler: suspend PointerInputScope.() -> Unit,
): suspend PointerInputScope.() -> Unit {
    return remember(isInteractionEnabled) {
        when (isInteractionEnabled) {
            true -> {
                onDragHandler
            }
            false -> {
                NoOpPointerInput
            }
        }
    }
}

@Composable
private fun rememberRotationOrigin(
    currentOrientation: Orientation,
    listSize: Dp,
): TransformOrigin {
    return remember(currentOrientation, listSize) {
        val origin = rotationOrigin(listSize)
        when (currentOrientation) {
            Orientation.HORIZONTAL -> TransformOrigin(origin, 0.5f)
            Orientation.VERTICAL -> TransformOrigin(0.5f, origin)
        }
    }
}

private fun Modifier.rotateItemRotation(currentOrientation: Orientation): Modifier {
    return this.then(
        rotate(
            when (currentOrientation) {
                Orientation.HORIZONTAL -> -90f
                Orientation.VERTICAL -> 0.0f
            }
        )
    )
}

private fun Modifier.cardSize(orientation: Orientation): Modifier {
    return when (orientation) {
        Orientation.HORIZONTAL -> this.then(height(OneItemSize))
        Orientation.VERTICAL -> this.then(width(OneItemSize))
    }
}

@Composable
private fun rememberDividerFactory(currentOrientation: Orientation): @Composable () -> Unit {
    return remember<@Composable () -> Unit>(currentOrientation) {
        when (currentOrientation) {
            Orientation.HORIZONTAL -> {
                {
                    val dividerColor = ToolkitBarStyle.Colors.onBackground.copy(alpha = ToolkitBarStyle.DividerAlpha)
                    VerticalDivider(color = dividerColor)
                }
            }
            Orientation.VERTICAL -> {
                {
                    val dividerColor = ToolkitBarStyle.Colors.onBackground.copy(alpha = ToolkitBarStyle.DividerAlpha)
                    Divider(color = dividerColor)
                }
            }
        }
    }
}

@Composable
private fun rememberListFactory(
    currentOrientation: Orientation,
    listState: LazyListState,
    animatedListSize: State<Dp>,
): @Composable (listContent: LazyListScope.() -> Unit) -> Unit {
    return remember<@Composable (listContent: LazyListScope.() -> Unit) -> Unit>(currentOrientation) {
        when (currentOrientation) {
            Orientation.HORIZONTAL -> {
                { listContent ->
                    LazyRow(
                        state = listState,
                        content = listContent,
                        modifier = Modifier.width(animatedListSize.value)
                    )
                }
            }
            Orientation.VERTICAL -> {
                { listContent ->
                    LazyColumn(
                        state = listState,
                        content = listContent,
                        modifier = Modifier.height(animatedListSize.value)
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberListContainerFactory(
    currentOrientation: Orientation,
): @Composable (content: @Composable () -> Unit) -> Unit {
    return remember<@Composable (content: @Composable () -> Unit) -> Unit>(currentOrientation) {
        when (currentOrientation) {
            Orientation.HORIZONTAL -> {
                { content -> Row { content() } }
            }
            Orientation.VERTICAL -> {
                { content -> Column { content() } }
            }
        }
    }
}

@Composable
private fun rememberListSize(itemsSize: Int): Dp {
    return remember(itemsSize) {
        if (itemsSize <= 0) {
            0.dp
        } else {
            OneItemSize * itemsSize.toFloat().coerceAtMost(4.5f) + 1.dp
        }
    }
}

@Stable
private fun rotationOrigin(listSize: Dp): Float {
    val totalDividerSize = when (listSize.value) {
        0.0f -> 1.0f
        else -> 2.0f
    }
    val itemSize = OneItemSize.value
    return (itemSize / 2) / (listSize.value + totalDividerSize + itemSize * 2)
}

val NoOpPointerInput: suspend PointerInputScope.() -> Unit = {
    /* no-op */
}

@Immutable
private data class PreviewMenuItem(
    @DrawableRes
    override val icon: Int,
    override val isEnabled: Boolean = true,
) : ToolkitBarMenuItem.IconOnly

@Suppress("UpdateTransitionLabel")
@Preview
@Composable
private fun PreviewToolkitBar() {
    MaterialTheme {
        val (orientation, onOrientationChange) = remember {
            mutableStateOf(Orientation.VERTICAL)
        }
        ToolkitBar(
            menuItems = listOf(
                PreviewMenuItem(R.drawable.qatoolkit_toolkitbar_ic_stroke_size_24dp),
                PreviewMenuItem(R.drawable.qatoolkit_toolkitbar_ic_stroke_color_24dp),
                PreviewMenuItem(R.drawable.qatoolkit_toolkitbar_ic_stroke_size_24dp),
                PreviewMenuItem(R.drawable.qatoolkit_toolkitbar_ic_stroke_color_24dp),
                PreviewMenuItem(R.drawable.qatoolkit_toolkitbar_ic_stroke_size_24dp),
            ),
            orientationTransition = updateTransition(orientation),
            onOrientationChange = onOrientationChange,
            onDragHandler = {},
            onMenuItemClick = { },
        )
    }
}

