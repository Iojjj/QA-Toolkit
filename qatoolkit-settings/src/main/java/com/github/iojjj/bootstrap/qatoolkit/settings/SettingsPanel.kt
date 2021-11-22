package com.github.iojjj.bootstrap.qatoolkit.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.iojjj.bootstrap.qatoolkit.settings.style.SettingsStyle
import kotlin.math.roundToInt

private val SPACE_SIZE = 16.dp
private val COLOR_PICKER_HEIGHT = 120.dp
private val CONTENT_MAX_SIZE = COLOR_PICKER_HEIGHT + SPACE_SIZE * 2
private val CARD_HEIGHT = CONTENT_MAX_SIZE +
    // top padding + space below title + space below content
    SPACE_SIZE * 3 +
    // extra to partially see 4th menu item
    8.dp


@OptIn(ExperimentalAnimationApi::class)
@ExperimentalMaterialApi
@Composable
fun <T : SettingsMenuItem> SettingsPanel(
    menuItems: List<T>,
    menuItemKey: ((T) -> Any)? = null,
    menuItemContent: @Composable (T) -> Unit,
    onBottomSheetStateChange: (BottomSheetValue) -> Unit,
) {
    require(menuItems.isNotEmpty()) {
        "At least one menu item required."
    }
    val (selectedMenuItem, onMenuItemClicked) = remember { mutableStateOf(menuItems.first()) }
    val swipeableState = rememberSwipeableState(BottomSheetValue.Expanded)
    val onOffsetChange = remember<Density.() -> IntOffset> {
        {
            IntOffset(0, swipeableState.offset.value.roundToInt())
        }
    }
    val density = LocalDensity.current
    val anchors = remember(density) {
        val cardHeight = with(density) {
            CARD_HEIGHT.toPx()
        }
        mapOf(
            0.0f to BottomSheetValue.Expanded,
            cardHeight to BottomSheetValue.Collapsed,
        )
    }
    val contentFactory = remember<@Composable AnimatedVisibilityScope.(targetState: T) -> Unit> {
        {
            menuItemContent(it)
        }
    }
    Card(
        elevation = SettingsStyle.CardElevation,
        modifier = Modifier
            .height(CARD_HEIGHT)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Vertical,
                resistance = null,
            )
            .offset(onOffsetChange)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = SPACE_SIZE, top = SPACE_SIZE, end = SPACE_SIZE)
        ) {
            Text(
                text = stringResource(selectedMenuItem.title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row {
                MenuList(
                    menuItems = menuItems,
                    menuItemKey = menuItemKey,
                    selectedMenuItem = selectedMenuItem,
                    onMenuItemClicked = onMenuItemClicked,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                Spacer(Modifier.width(SPACE_SIZE))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        // To align with icons.
                        .padding(end = 12.dp)
                ) {
                    AnimatedContent(
                        targetState = selectedMenuItem,
                        content = contentFactory,
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }

    LaunchedEffect(swipeableState.currentValue) {
        onBottomSheetStateChange(swipeableState.currentValue)
    }
}