package com.github.iojjj.bootstrap.qatoolkit.inspector

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.iojjj.bootstrap.qatoolkit.insets.rememberInsetsPaddingValues
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.InspectorDetails
import com.github.iojjj.bootstrap.qatoolkit.inspector.details.InspectorDetailsState
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.derivedWindowInsetsTypeOf

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun NodeDetails(viewModel: InspectorModeViewModel) {
    val detailsState = viewModel.details.collectAsState()
    val searchQueryState = viewModel.attributesSearchQuery.collectAsState()
    val newState = detailsState.value
    val previousState = remember {
        mutableStateOf(newState)
    }
    AnimatedVisibility(
        visible = newState != null,
        enter = fadeIn(animationSpec = tween(delayMillis = DefaultDurationMillis)),
        exit = fadeOut()
    ) {
        val state = newState
            ?: previousState.value
            ?: InspectorDetailsState.Loading

        val windowInsets = LocalWindowInsets.current
        val statusBars = windowInsets.statusBars
        val navBars = windowInsets.navigationBars
        val ime = windowInsets.ime
        val insets = remember(statusBars, ime, navBars) {
            derivedWindowInsetsTypeOf(statusBars, navBars, ime)
        }

        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(
                    rememberInsetsPaddingValues(
                        insets = insets,
                        applyStart = true,
                        applyEnd = true,
                        applyBottom = true,
                        additional = 24.dp
                    )
                )
        ) {
            Card {
                InspectorDetails(
                    state = state,
                    searchQuery = searchQueryState.value,
                    onSearchQueryChange = viewModel.onSearchQueryChange,
                    onDismissRequest = viewModel.onDetailsDismiss
                )
            }
        }

        previousState.value = state
    }
}