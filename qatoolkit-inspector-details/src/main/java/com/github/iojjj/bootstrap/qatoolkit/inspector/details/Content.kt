package com.github.iojjj.bootstrap.qatoolkit.inspector.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.iojjj.bootstrap.pub.core.exhaustive

@Composable
internal fun InspectorDetailsContent(
    state: InspectorDetailsState,
    searchQuery: String,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        exhaustive..when (state) {
            InspectorDetailsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            is InspectorDetailsState.Loaded -> {
                if (state.categorizedAttributes.isEmpty()) {
                    EmptyListMessage(searchQuery)
                } else {
                    AttributesList(
                        state.categorizedAttributes,
                        searchQuery
                    )
                }
            }
        }
    }
}

